package com.example.digital_contest.activity.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.digital_contest.activity.sphash.boardDB
import com.example.digital_contest.model.Board
import com.example.digital_contest.model.User
import com.example.digital_contest.model.db.Board.BoardResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ViewActivityViewModel : ViewModel() {
    lateinit var boardId : String
    val boardData = MutableLiveData<Board>(Board())
    lateinit var userData : User


    var userPoolLike = MutableLiveData(false)

    fun getBoardData(){
        CoroutineScope(Dispatchers.Main).launch {
            boardData.value = CoroutineScope(Dispatchers.IO).async{
                boardDB.getBoardById(boardId)!!
            }.await()
        }
    }

    suspend fun likeAdd() : BoardResult {
        boardData.value!!.likeUserList.add(userData.id)
        return boardDB.boardLikeListUpdate(boardData.value!!.likeUserList, boardId)
    }

    suspend fun likeCancel() : BoardResult{
        boardData.value!!.likeUserList.remove(userData.id)
        return boardDB.boardLikeListUpdate(boardData.value!!.likeUserList, boardId)
    }

}