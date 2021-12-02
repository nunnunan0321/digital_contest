package com.example.digital_contest.model

import androidx.annotation.Keep
import com.google.firebase.firestore.GeoPoint
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

@Keep
data class Board(
    val title : String = "", //게시물 제목
    val writerID : String = "", //작성자 ID
    val contents : String = "", //게시물의 내용
    val location: GeoPoint = GeoPoint(0.0, 0.0), // 게시물의 위치 정보
    var imgUrl : String = "", //Firebase Storage

    var likeUuserList : ArrayList<String> = arrayListOf(), //좋아요 누른 유저 id저장

    val uploadDate : Date = Date(), //게시물 작성일
) : Serializable
