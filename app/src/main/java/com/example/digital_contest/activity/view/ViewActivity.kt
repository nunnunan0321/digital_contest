package com.example.digital_contest.activity.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.digital_contest.R
import com.example.digital_contest.activity.sphash.boardDB
import com.example.digital_contest.activity.write.WriteActivityViewModel
import com.example.digital_contest.databinding.ActivityViewBinding
import com.example.digital_contest.model.db.Board.board
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityViewBinding
    lateinit var viewModel: WriteActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view)

        val boardId = intent.getStringExtra("boardId").toString()

        CoroutineScope(Dispatchers.IO).launch {
            val boardData = boardDB.getBoardById(boardId)

            if(boardData == null){
                Toast.makeText(this@ViewActivity, "없는 게시물 입니다.", Toast.LENGTH_SHORT).show()
                finish()
                return@launch
            }

            withContext(Dispatchers.Main){
                binding.txtViewBoardTitle.text = boardData.title
                binding.txtViewWriter.text = "작성자 : ${boardData.writerID}"
                binding.txtViewBoardContent.text = boardData.contents
            }
        }
    }
}
