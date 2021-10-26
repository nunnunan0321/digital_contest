package com.example.digital_contest.model

import java.io.Serializable

data class User(
    val id : String = "",
    val name : String = "",
    val email : String = "",
)  : Serializable
