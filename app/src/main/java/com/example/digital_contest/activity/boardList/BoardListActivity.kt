package com.example.digital_contest.activity.boardList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.digital_contest.BoardListAdapter
import com.example.digital_contest.R
import com.example.digital_contest.activity.sphash.boardDB
import com.example.digital_contest.databinding.ActivityBoardListBinding
import com.example.digital_contest.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoardListActivity : AppCompatActivity() {
    lateinit var binding : ActivityBoardListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_list)

        val userData = intent.getSerializableExtra("userData") as User

        CoroutineScope(Dispatchers.IO).launch {
            val boardList = boardDB.getBoardByUserId(userData.id)
            withContext(Dispatchers.Main){
                binding.recyclerBoardList.adapter = BoardListAdapter(boardList, userData)
            }
        }

        val boardListDecoration = DividerItemDecoration(this, LinearLayoutManager(this).orientation)
        binding.recyclerBoardList.addItemDecoration(boardListDecoration)

        binding.imgBoardListArrow.setOnClickListener {
            finish()
        }
    }
}