package com.example.digital_contest.model

import java.io.Serializable

data class User(
    var id : String = "",
    var name : String = "",
    var email : String = "",

    var userMSG : String = "",
    var profileImgUrl : String = ""
)  : Serializable
