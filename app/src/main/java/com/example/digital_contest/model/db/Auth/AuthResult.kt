package com.example.digital_contest.model.db.Auth

import com.google.firebase.auth.AuthResult

enum class AuthResult {
    OK,
    Fail,
    IdOverlap,
}