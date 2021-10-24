package com.example.digital_contest.Model

import com.google.firebase.firestore.GeoPoint
import java.io.Serializable

data class Board(
    val writerID : String, //작성자 ID
    val contents : String, //게시물의 내용
    val lickCount : Int, //게시물의 좋아요 수
//    val position  : GeoPoint //위치정보를 저장
) : Serializable
