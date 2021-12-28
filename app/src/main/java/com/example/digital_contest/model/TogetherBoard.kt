package com.example.digital_contest.model

import com.google.firebase.firestore.GeoPoint
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

data class TogetherBoard(
    val rootBoardId : String = "",

    val title : String = "",
    val writerID : String = "",
    val contents : String = "",
    var imgUrl : String = "",

    var likeUuserList : ArrayList<String> = arrayListOf(),

    val uploadDate : Date = Date(),
) : Serializable
