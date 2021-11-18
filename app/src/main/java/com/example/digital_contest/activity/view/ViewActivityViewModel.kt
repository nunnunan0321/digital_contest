package com.example.digital_contest.activity.view

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.digital_contest.activity.sphash.authDB
import com.example.digital_contest.activity.sphash.boardDB
import com.example.digital_contest.model.Board
import com.example.digital_contest.model.User
import com.example.digital_contest.model.db.Board.BoardResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.DateFormat

class ViewActivityViewModel : ViewModel() {
    lateinit var boardId : String
    val boardData = MutableLiveData(Board())
    val uploadDateFormat = MutableLiveData("")

    lateinit var currentUserData : User

    val writerUserData = MutableLiveData(User())

    var userPoolLike = MutableLiveData(false)

    fun getBoardData(){
        CoroutineScope(Dispatchers.Main).launch {
            boardData.value = CoroutineScope(Dispatchers.IO).async{
                boardDB.getBoardById(boardId)!!
            }.await()
            uploadDateFormat.value = DateFormat.getDateInstance(DateFormat.MEDIUM).format(boardData.value!!.uploadDate)
            getWriterUserData()
        }
    }

    fun getWriterUserData(){
        Log.d("writerID", boardData.value!!.writerID)
        CoroutineScope(Dispatchers.Main).launch {
            writerUserData.value = CoroutineScope(Dispatchers.IO).async {
                authDB.getUserDataById(boardData.value!!.writerID)!!
            }.await()
        }
    }

    suspend fun addLike() : BoardResult{
        currentUserData.likeBoardList.add(boardId)
        return boardDB.likeListUpdate(currentUserData.likeBoardList, currentUserData.id)
    }

    suspend fun cancelLike() : BoardResult{
        currentUserData.likeBoardList.remove(boardId)
        return boardDB.likeListUpdate(currentUserData.likeBoardList, currentUserData.id)
    }
}