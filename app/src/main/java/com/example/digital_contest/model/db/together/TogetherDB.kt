package com.example.digital_contest.model.db.together

import android.net.Uri
import com.example.digital_contest.model.TogetherBoard
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class TogetherDB : together{
    val db = FirebaseFirestore.getInstance()
    val storage : FirebaseStorage = FirebaseStorage.getInstance()
    val storageRef = storage.reference

    override suspend fun saveBoard(together: TogetherBoard, img : Uri): TogetherResult {
        val imgResult = saveImg(img, together.writerID) ?: return TogetherResult.Fail

        together.imgUrl = imgResult

        var result = TogetherResult.Fail

        db.collection("togetherBoard").add(together)
            .addOnSuccessListener {
                result = TogetherResult.OK
            }.await()

        if(result == TogetherResult.Fail) return result

        return TogetherResult.OK
    }

    override suspend fun getAllBoardBtRootId(rootBoardID: String): Map<String, TogetherBoard> {
        val result = mutableMapOf<String, TogetherBoard>()

        db.collection("togetherBoard").whereEqualTo("rootBoardId", rootBoardID).get()
            .addOnSuccessListener { documents ->
                for(document in documents){
                    result[document.id] = document.toObject(TogetherBoard::class.java)
                }
            }.await()

        return result
    }

    override suspend fun saveImg(img: Uri, writerId: String): String? {
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
}