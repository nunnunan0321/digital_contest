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
        val userDataScope = CoroutineScope(Dispatchers.IO).async {
            addUserLikeData()
        }
        val boardDataScope = CoroutineScope(Dispatchers.IO).async {
            addBoardLikeList()
        }

        return userDataScope.await() == BoardResult.OK && boardDataScope.await() == BoardResult.OK
    }

    suspend fun addUserLikeData() : BoardResult {
        currentUserData.likeBoardList.add(boardId)
        writerUserData.value!!.totalLikeCount++

        return boardDB.userLikeListUpdate(currentUserData.likeBoardList, currentUserData.id)
    }

    suspend fun addBoardLikeList() : BoardResult{
        boardData.value!!.likeUuserList.add(currentUserData.id)

        return boardDB.boardLikeListUpdate(boardData.value!!.likeUuserList, boardId)
    }


    suspend fun cancelLike() : Boolean {
        val userDataScope = CoroutineScope(Dispatchers.IO).async {
            cancelUserLikeData()
        }
        val boardDataScope = CoroutineScope(Dispatchers.IO).async {
            cancelBoardLikeList()
        }

        return userDataScope.await() == BoardResult.OK && boardDataScope.await() == BoardResult.OK
    }

    suspend fun cancelUserLikeData() : BoardResult{
        currentUserData.likeBoardList.remove(boardId)
        writerUserData.value!!.totalLikeCount--

        return boardDB.userLikeListUpdate(currentUserData.likeBoardList, currentUserData.id)
    }

    suspend fun cancelBoardLikeList() : BoardResult{
        boardData.value!!.likeUuserList.remove(currentUserData.id)

        return boardDB.boardLikeListUpdate(boardData.value!!.likeUuserList, boardId)
    }
}