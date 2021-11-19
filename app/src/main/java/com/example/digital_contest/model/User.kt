package com.example.digital_contest.model

import java.io.Serializable

data class User(
    var id : String = "", //유저의 id
    var name : String = "", //사용자 이름
    var email : String = "", //사용자 이메일

    var userMSG : String = "", //사용자의 소개 메시지
    var profileImgUrl : String = "", //Storage에 저장된 사용 프로필 사진

    var likeBoardList : ArrayList<String> = arrayListOf(), //사용자가 좋아요 누른 게시물의 id
    var totalLikeCount : Int = 0 //사용자가 지금까지 받은 좋아요 개수
)  : Serializable
