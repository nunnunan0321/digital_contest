package com.example.digital_contest.activity.view

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.digital_contest.activity.sphash.authDB
import com.example.digital_contest.activity.sphash.boardDB
import com.example.digital_contest.activity.sphash.commentDB
import com.example.digital_contest.model.Board
import com.example.digital_contest.model.Comment
import com.example.digital_contest.model.User
import com.example.digital_contest.model.db.Board.BoardResult
import com.example.digital_contest.model.db.Comment.CommentResult
import kotlinx.coroutines.*
import java.text.DateFormat

class ViewActivityViewModel : ViewModel() {
    lateinit var boardId : String
    val boardData = MutableLiveData(Board())
    val uploadDateFormat = MutableLiveData("")
    val commentContent = MutableLiveData("")

    lateinit var currentUserData : User

    val writerUserData = MutableLiveData(User())

    var userPoolLike = MutableLiveData(false)

    val commentList = MutableLiveData<MutableList<Comment>>()

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
        //Log.d("writerID", boardData.value!!.writerID)
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
        val writerDataScope = CoroutineScope(Dispatchers.IO).async {
            addWriterLikeCount()
        }
        val boardDataScope = CoroutineScope(Dispatchers.IO).async {
            addBoardLikeList()
        }

        return userDataScope.await() && writerDataScope.await() && boardDataScope.await()
    }

    suspend fun addUserLikeData() : Boolean {
        currentUserData.likeBoardList.add(boardId)
        return boardDB.readersLikeListUpdate(currentUserData.likeBoardList, currentUserData.id) == BoardResult.OK
    }

    suspend fun addBoardLikeList() : Boolean{
        boardData.value!!.likeUuserList.add(currentUserData.id)
        return boardDB.boardLikeListUpdate(boardData.value!!.likeUuserList, boardId) == BoardResult.OK
    }

    suspend fun addWriterLikeCount() : Boolean{
        writerUserData.value!!.totalLikeCount++
        return boardDB.writerLikeCountUpdate(writerUserData.value!!.totalLikeCount, writerUserData.value!!.id) == BoardResult.OK
    }

    suspend fun cancelLike() : Boolean {
        val userDataScope = CoroutineScope(Dispatchers.IO).async {
            cancelUserLikeData()
        }
        val writerDataScope = CoroutineScope(Dispatchers.IO).async {
            cancelWriterLikeData()
        }
        val boardDataScope = CoroutineScope(Dispatchers.IO).async {
            cancelBoardLikeList()
        }

        return userDataScope.await() && writerDataScope.await() && boardDataScope.await()
    }

    suspend fun cancelUserLikeData() : Boolean{
        currentUserData.likeBoardList.remove(boardId)
        return boardDB.readersLikeListUpdate(currentUserData.likeBoardList, currentUserData.id) == BoardResult.OK
    }

    suspend fun cancelWriterLikeData() : Boolean{
        writerUserData.value!!.totalLikeCount--
        return boardDB.writerLikeCountUpdate(writerUserData.value!!.totalLikeCount, writerUserData.value!!.id) == BoardResult.OK
    }

    suspend fun cancelBoardLikeList() : Boolean{
        boardData.value!!.likeUuserList.remove(currentUserData.id)
        return boardDB.boardLikeListUpdate(boardData.value!!.likeUuserList, boardId) == BoardResult.OK
    }

    suspend fun writeComment() : Comment?{
        val commentData = Comment(
            writerID = currentUserData.id,
            content = commentContent.value!!,

            boardID = boardId,
        )

        val result = commentDB.writerComment(commentData)
        return if(result == CommentResult.Fail) null else commentData
    }

    suspend fun getComment() {
        commentList.value = commentDB.getAllCommentByBoardId(boardId)
    }
}