package com.example.digital_contest.model.db.Board

import android.net.Uri
import com.example.digital_contest.model.Board
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BoardDB : board {
    val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    val storage : FirebaseStorage = FirebaseStorage.getInstance()
    val storageRef = storage.reference

    override suspend fun saveBoard(boardData: Board, img: Uri): BoardResult {
        var result : BoardResult = BoardResult.Fail

        val saveImgResult = saveBoardImage(img, boardData.writerID) ?: return result

        boardData.imgUrl = saveImgResult

        db.collection("board").add(boardData)
            .addOnSuccessListener {
                result = BoardResult.OK
            }
            .await()

        return result
    }

    override suspend fun saveBoardImage(img: Uri, writerId : String): String? {
        var downloadUri : String? = null

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageName = "${timeStamp}_${writerId}.png"

        val imgRef = storageRef.child("board").child(imageName)

        imgRef.putFile(img)
            .continueWithTask{
                return@continueWithTask imgRef.downloadUrl
            }
            .addOnSuccessListener {
                downloadUri = it.toString()
            }
            .await()

        return downloadUri
    }

    override suspend fun getAllBoard(): Map<String, Board> {
        val boardList = mutableMapOf<String, Board>()
        db.collection("board").get()
            .addOnSuccessListener {
                for (document in it){
                    boardList[document.id] = document.toObject(Board::class.java)
                }
            }.await()

        return boardList
    }

    override suspend fun getBoardById(id: String) : Board? {
        var boardResult : Board? = null
        db.collection("board").document(id).get()
            .addOnSuccessListener {
                boardResult = it.toObject(Board::class.java)
            }.await()

        return boardResult
    }

    override suspend fun getBoardByUserId(userId: String): Map<String, Board> {
        val result = mutableMapOf<String, Board>()

        db.collection("board").whereEqualTo("writerID", userId)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents){
                    result[document.id] = document.toObject(Board::class.java)
                }
            }.await()

        return result
    }

    override suspend fun readersLikeListUpdate(likeBoardList: ArrayList<String>, readersUserID: String): BoardResult {
        var result : BoardResult = BoardResult.Fail

        db.collection("user").document(readersUserID)
            .update("likeBoardList", likeBoardList)
            .addOnSuccessListener {
                result = BoardResult.OK
            }
            .await()

        return result
    }

    override suspend fun writerLikeCountUpdate(totalLikeCount: Int, writerId: String): BoardResult {
        var result = BoardResult.Fail

        db.collection("user").document(writerId)
            .update("totalLikeCount", totalLikeCount)
            .addOnSuccessListener {
                result = BoardResult.OK
            }
            .await()

        return result
    }

    override suspend fun boardLikeListUpdate(
        likeUserList: ArrayList<String>,
        boardID: String
    ): BoardResult {
        var result = BoardResult.Fail

        db.collection("board").document(boardID)
            .update("likeUuserList", likeUserList)
            .addOnSuccessListener {
                result = BoardResult.OK
            }
            .await()

        return result
    }

    override suspend fun getLikeBoards(likeBoardsList : ArrayList<String>): Map<String, Board> {
        val result = mutableMapOf<String, Board>()

        for(i in likeBoardsList){
            db.collection("board").document(i).get()
                .addOnSuccessListener {
                    result.put(it.id, it.toObject(Board::class.java)!!)
                }
                .await()
        }

//        Log.d("likeList", "함수 종료")
        
        return result
    }
}