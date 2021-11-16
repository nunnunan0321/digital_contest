package com.example.digital_contest.activity.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.digital_contest.R
import com.example.digital_contest.activity.sphash.boardDB
import com.example.digital_contest.activity.write.WriteActivityViewModel
import com.example.digital_contest.databinding.ActivityViewBinding
import com.example.digital_contest.model.User
import com.example.digital_contest.model.db.Board.BoardResult
import com.example.digital_contest.model.db.Board.board
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

        viewModel.userData = intent.getSerializableExtra("userData") as User
        viewModel.boardId = intent.getStringExtra("boardId").toString()
        viewModel.getBoardData()

        binding.imgViewLikeBtn.setOnClickListener{ //좋아요 버튼을 눌렀을때

            Log.d("likeTest click", "하트 누름")

            CoroutineScope(Dispatchers.Main).launch {
                if(!(viewModel.userPoolLike.value!!)){ //이전에 좋아요를 누르지 않았다면
                    binding.imgViewLikeBtn.setImageResource(R.drawable.ic_heart_fill)
                    val addLikeResult = viewModel.likeAdd() //해당 게시물의 좋아요 목록 자신의 ID를 추가한다.

                    if(addLikeResult == BoardResult.OK){ //좋아요 목록에 자신의 ID추가에 성공했다면
                        Toast.makeText(this@ViewActivity, "게시물에 좋아요를 추가하였습니다.", Toast.LENGTH_SHORT).show()
                    }

                }   else{ //이전에 좋아요를 눌렀다면
                    binding.imgViewLikeBtn.setImageResource(R.drawable.ic_heart)
                    val calcelLikeResult = viewModel.likeCancel() //해당 게시물의 좋아요 목록에 자신의 ID를 취소한다.

                    if(calcelLikeResult == BoardResult.OK){
                        Toast.makeText(this@ViewActivity, "게시물에 좋아요를 취소했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                viewModel.userPoolLike.value = viewModel.boardData.value!!.likeUserList.contains(viewModel.userData.id)
                Log.d("likeTest observe board", viewModel.userPoolLike.value.toString())
                Log.d("likeTest pollLike", viewModel.userPoolLike.value.toString())
                Log.d("likeTest arr", viewModel.boardData.value!!.likeUserList.toString())
            }
        }

        viewModel.boardData.observe(this, {
            // 게시물을 가져왔을때 사진을 띄운다.
            Glide.with(this).load(it.imgUrl).into(binding.imgViewBoardImage)
        })

        viewModel.userPoolLike.observe(this, { //좋아요를 누르고 취소했을때 좋아요 아이콘을 바꾼다.
            if(it){
                Log.d("likeTest observe", "추가됨")
                binding.imgViewLikeBtn.setImageResource(R.drawable.ic_heart_fill)
            }   else{
                Log.d("likeTest observe", "취소됨")
                binding.imgViewLikeBtn.setImageResource(R.drawable.ic_heart)
            }
        })
    }
}
