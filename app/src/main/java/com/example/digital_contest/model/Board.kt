package com.example.digital_contest.model

import androidx.annotation.Keep
import com.google.firebase.firestore.GeoPoint
import java.io.Serializable
import java.util.*

@Keep
data class Board(
    val title : String = "", //게시물 제목
    val writerID : String = "", //작성자 ID
    val contents : String = "", //게시물의 내용
    val likeCount : Array<Int> = arrayOf(), //게시물의 좋아요 수

//    val uploadDate: FieldValue = FieldValue.serverTimestamp(), //글을 쓴 날짜 저장
    val uploadDate : Date? = Date(),
    val location: GeoPoint = GeoPoint(0.0, 0.0) //위치정보를 저장
//    val imgUrl : String
) : Serializable
