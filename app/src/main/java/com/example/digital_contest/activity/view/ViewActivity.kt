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

        viewModel.boardId = MutableLiveData(intent.getStringExtra("boardId").toString())
        viewModel.getBoardData()

        binding.imgViewLikeBtn.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch {
                val addLikeResult = viewModel.likeAdd()

                if(addLikeResult == BoardResult.OK){
                    Toast.makeText(this@ViewActivity, "게시물에 좋아요를 추가하였습니다.", Toast.LENGTH_LONG).show()
                    binding.imgViewLikeBtn.setImageResource(R.drawable.ic_heart_fill)
                }
            }
        }

        viewModel.boardData.observe(this, {
//            Log.d("getBoard", "게시물 가져옴 ${it.imgUrl}")
            Glide.with(this).load(it.imgUrl).into(binding.imgViewBoardImage)
        })
    }
}
