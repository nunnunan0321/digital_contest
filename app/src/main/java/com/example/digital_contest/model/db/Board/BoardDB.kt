package com.example.digital_contest.model.db.Board

import com.example.digital_contest.model.Board
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BoardDB : board {
    val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun saveBoard(boardData: Board): BoardResult {
        var result : BoardResult = BoardResult.OK

        db.collection("board").add(boardData)
            .addOnFailureListener {
                result = BoardResult.Fail
            }
            .await()

        return result
    }
}