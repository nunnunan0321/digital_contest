package com.example.digital_contest.model

import java.io.Serializable
import java.util.*

data class Comment(
    val writerID : String = "",
    val content : String = "",

    val boardID : String = "",

    val timeStamp : Date = Date(),
) : Serializable
