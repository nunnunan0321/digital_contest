package com.example.digital_contest.activity.view

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.digital_contest.activity.sphash.authDB
import com.example.digital_contest.activity.sphash.boardDB
import com.example.digital_contest.model.Board
import com.example.digital_contest.model.User
import com.example.digital_contest.model.db.Auth.AuthResult
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

    suspend fun addLike() : Boolean{
        currentUserData.likeBoardList.add(boardId)
        writerUserData.value!!.totalLikeCount++

        val addUserLikeCountResult = authDB.userTotalLikeCountUpdate(writerUserData.value!!)
        if(addUserLikeCountResult == AuthResult.Fail) return false

        val likeListResult =  boardDB.likeListUpdate(currentUserData.likeBoardList, currentUserData.id)
        if(likeListResult == BoardResult.Fail){
            writerUserData.value!!.totalLikeCount--
            authDB.userTotalLikeCountUpdate(writerUserData.value!!)
            return false
        }

        return true
    }

    suspend fun cancelLike() : Boolean {
        currentUserData.likeBoardList.remove(boardId)
        writerUserData.value!!.totalLikeCount--

        val userTotalLikeCountResult = authDB.userTotalLikeCountUpdate(writerUserData.value!!)
        if(userTotalLikeCountResult == AuthResult.Fail) return false

        val likeListResult = boardDB.likeListUpdate(currentUserData.likeBoardList, currentUserData.id)
        if(likeListResult == BoardResult.Fail){
            writerUserData.value!!.totalLikeCount++
            authDB.userTotalLikeCountUpdate(writerUserData.value!!)
            return false
        }

        return true
    }
}