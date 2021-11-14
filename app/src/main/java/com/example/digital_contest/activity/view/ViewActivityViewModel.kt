package com.example.digital_contest.activity.view

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.digital_contest.activity.sphash.boardDB
import com.example.digital_contest.model.Board
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ViewActivityViewModel() : ViewModel() {
    lateinit var boardId : MutableLiveData<String>
    val boardData = MutableLiveData<Board>()

    var boardTitle = MutableLiveData<String>("")
    var boardContent = MutableLiveData<String>("")
    var boardWriter = MutableLiveData<String>("")


    fun getBoardData(){
        CoroutineScope(Dispatchers.Main).launch {
            boardData.value = CoroutineScope(Dispatchers.IO).async{
                boardDB.getBoardById(boardId.value.toString())!!
            }.await()

            boardTitle.value = boardData.value!!.title
            boardContent.value = boardData.value!!.contents
            boardWriter.value = boardData.value!!.writerID
        }
    }
}