package com.example.digital_contest.model.db.Auth

import android.net.Uri
import com.example.digital_contest.model.User

interface auth {
    suspend fun signUp(user : User, password : String, profileImg : Uri?) : AuthResult
    suspend fun createAccount(user : User, password: String) : AuthResult
    suspend fun saveProfileImg(img : Uri, userID : String) : String?
    suspend fun saveUserData(user: User) : AuthResult

    suspend fun idOverlapCheck(user : User) : AuthResult

    suspend fun login(id : String, password : String) : User?
    suspend fun getUserDataById(id : String) : User?
    suspend fun getUserDataByEmail(email : String) : User?

    suspend fun userTotalLikeCountUpdate(user: User) : AuthResult
}