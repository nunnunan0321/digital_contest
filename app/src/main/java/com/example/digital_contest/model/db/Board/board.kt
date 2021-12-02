package com.example.digital_contest.model.db.Board

import android.net.Uri
import com.example.digital_contest.model.Board
import com.firebase.geofire.GeoLocation

interface board {
    suspend fun saveBoard(boardData : Board, img : Uri) : BoardResult
    suspend fun saveBoardImage(img : Uri, writerId : String) : String?

    suspend fun getAllBoard() : Map<String, Board>
    suspend fun getBoardById(id : String) : Board?
    suspend fun getBoardByUserId(id : String) : Map<String, Board>

    suspend fun userLikeListUpdate(likeBoardList : ArrayList<String>, userID : String) : BoardResult
    suspend fun boardLikeListUpdate(likeUserList : ArrayList<String>, boardID : String) : BoardResult
    suspend fun getLikeBoards(likeBoardsList : ArrayList<String>) : Map<String, Board>
}