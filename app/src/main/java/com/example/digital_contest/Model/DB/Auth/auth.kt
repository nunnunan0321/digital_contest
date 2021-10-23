package com.example.digital_contest.Model.DB.Auth

import com.example.digital_contest.Model.User

interface auth {
    suspend fun signUp(user : User, password : String) : AuthResult

    suspend fun createAccount(user : User, password: String) : AuthResult

    suspend fun saveUserData(user: User) : AuthResult

    suspend fun idOverlapCheck(user : User) : AuthResult

    suspend fun login(id : String, password : String) : User?

    suspend fun getUserDataById(id : String) : User?

    suspend fun getUserDataByEmail(email : String) : User?
}