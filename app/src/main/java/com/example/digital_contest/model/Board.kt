package com.example.digital_contest.model

import android.location.LocationListener
import com.firebase.geofire.GeoLocation
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.GeoPoint
import java.io.Serializable
import java.sql.Timestamp
import java.time.LocalDate

data class Board(
    val title : String, //게시물 제목
    val writerID : String, //작성자 ID
    val contents : String, //게시물의 내용
    val likeCount : Int = 0, //게시물의 좋아요 수

    val uploadDate: FieldValue, //글을 쓴 날짜 저장
    val location: GeoLocation //위치정보를 저장
//    val imgUrl : String,
//    val position  : GeoPoint //위치정보를 저장
) : Serializable
