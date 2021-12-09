package com.example.digital_contest.model.db.Comment

import com.example.digital_contest.model.Comment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CommentDB : comment {
    val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun writerComment(comment: Comment): CommentResult {
        var result = CommentResult.Fail

        db.collection("board").document(comment.boardID)
            .collection("comment").add(comment)
            .addOnSuccessListener {
                result = CommentResult.OK
            }
            .await()

        return result
    }
}