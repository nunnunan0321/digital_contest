package com.example.digital_contest.model.db.Board

import com.example.digital_contest.model.Board

interface board {
    suspend fun saveBoard(boardData : Board) : BoardResult
}