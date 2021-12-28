package com.example.digital_contest.activity.boardList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.digital_contest.BoardListAdapter
import com.example.digital_contest.R
import com.example.digital_contest.TogetherBoardListAdapter
import com.example.digital_contest.activity.sphash.boardDB
import com.example.digital_contest.activity.sphash.togetherDB
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
        val rootBoardId = intent.getSerializableExtra("rootBoardId") ?: "normal"

        CoroutineScope(Dispatchers.IO).launch {
            if(rootBoardId == "normal") {
                val boardList = boardDB.getBoardByUserId(userData.id)

                withContext(Dispatchers.Main){
                    binding.progressBoardListLoadingProgress.visibility = View.INVISIBLE
                    binding.recyclerBoardList.adapter = BoardListAdapter(boardList, userData)

                    if(boardList.isEmpty()){
                        binding.txtBoardListPlsMsg.visibility = View.VISIBLE
                    }
                }
            }
            else {
                val boardList = togetherDB.getAllBoardBtRootId(rootBoardId.toString())
                withContext(Dispatchers.Main){
                    binding.progressBoardListLoadingProgress.visibility = View.INVISIBLE
                    binding.recyclerBoardList.adapter = TogetherBoardListAdapter(boardList, userData)

                    if(boardList.isEmpty()){
                        binding.txtBoardListPlsMsg.visibility = View.VISIBLE
                    }
                }
            }

        }

        binding.imgBoardListPrevBtn.setOnClickListener {
            finish()
        }
    }
}