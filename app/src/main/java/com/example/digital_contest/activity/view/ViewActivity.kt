package com.example.digital_contest.activity.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.digital_contest.R
import com.example.digital_contest.databinding.ActivityViewBinding
import com.example.digital_contest.model.User
import com.example.digital_contest.model.db.Board.BoardResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityViewBinding
    lateinit var viewModel: ViewActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view)
        viewModel = ViewModelProvider(this).get(ViewActivityViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.currentUserData = intent.getSerializableExtra("userData") as User
        viewModel.boardId = intent.getStringExtra("boardId").toString()
        viewModel.getBoardData()

        binding.imgViewLikeBtn.setOnClickListener{ //좋아요 버튼을 눌렀을때
            CoroutineScope(Dispatchers.Main).launch {
                if(!(viewModel.userPoolLike.value!!)){ //이전에 좋아요를 누르지 않았다면
                    binding.imgViewLikeBtn.setImageResource(R.drawable.ic_heart_fill)

                    val addLikeResult = viewModel.addLike()

                    if(!addLikeResult){ //좋아요 목록에 자신의 ID추가에 성공했다면
                        Toast.makeText(this@ViewActivity, "게시물에 좋아요를 추가하지 못했습니다.", Toast.LENGTH_SHORT).show()
                    }

                }   else{ //이전에 좋아요를 눌렀다면
                    binding.imgViewLikeBtn.setImageResource(R.drawable.ic_heart)

                    val calcelLikeResult = viewModel.cancelLike()

                    if(!calcelLikeResult){
                        Toast.makeText(this@ViewActivity, "게시물에 좋아요를 취소하지못했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                viewModel.userPoolLike.value = viewModel.currentUserData.likeBoardList.contains(viewModel.boardId) //사용자가 현재게시물에 좋아요를 눌렀는지 확인
            }
        }

        binding.imgViewPrevBtn.setOnClickListener {
            finish()
        }

        binding.imgViewSendComment.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.writeComment()

                withContext(Dispatchers.Main){
                    binding.edtViewInputComment.setText("")
                    Toast.makeText(this@ViewActivity, "댓글을 추가했습니다.", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.boardData.observe(this){
            Glide.with(this).load(it.imgUrl).into(binding.imgViewBoardImage)// 게시물을 가져왔을때 사진을 띄운다.

            viewModel.userPoolLike.value = viewModel.currentUserData.likeBoardList.contains(viewModel.boardId) //사용자가 현재게시물에 좋아요를 눌렀는지 확인
        }

        viewModel.writerUserData.observe(this){
            Glide.with(this).load(it.profileImgUrl).into(binding.imgViewProfileImg)
        }

        viewModel.userPoolLike.observe(this){
            //좋아요를 누르고 취소했을때 좋아요 아이콘을 바꾼다.
            binding.imgViewLikeBtn.setImageResource(if(it) R.drawable.ic_heart_fill else R.drawable.ic_heart)
        }

        viewModel.commentContent.observe(this){
            if(it.isEmpty()) binding.imgViewSendComment.visibility = View.GONE
            else binding.imgViewSendComment.visibility = View.VISIBLE
        }

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getComment()

            //Log.d("commentList", viewModel.commentList.value.toString())
            val adapter = CommentListAdapter(viewModel.commentList.value!!)
            binding.recyclerViewCommentList.adapter = adapter
        }
    }
}
