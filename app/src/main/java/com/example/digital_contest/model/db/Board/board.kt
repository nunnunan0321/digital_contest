package com.example.digital_contest.model.db.Board

import com.example.digital_contest.model.Board
import com.firebase.geofire.GeoLocation

interface board {
    suspend fun saveBoard(boardData : Board) : BoardResult

    suspend fun getAllBoard() : List<Board>

//    suspend fun
}