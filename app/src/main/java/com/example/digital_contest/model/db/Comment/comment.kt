package com.example.digital_contest.model.db.Comment

import com.example.digital_contest.model.Comment

interface comment {
    suspend fun writerComment(comment: Comment) : CommentResult
    suspend fun getAllCommentByBoardId(boardId : String) : MutableList<Comment>
}