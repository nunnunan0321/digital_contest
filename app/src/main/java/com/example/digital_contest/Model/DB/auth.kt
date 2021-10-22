package com.example.digital_contest.Model.DB

import com.example.digital_contest.Model.User

interface auth {
    suspend fun signUp(user : User, password : String) : AuthResult

    suspend fun createAccount(user : User, password: String) : AuthResult

    suspend fun saveUserData(user: User) : AuthResult

    suspend fun idOverlapCheck(user : User) : AuthResult
}