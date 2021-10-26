package com.example.digital_contest.activity.sphash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.digital_contest.activity.login.LoginActivity
import com.example.digital_contest.model.db.Auth.AuthDB
import com.example.digital_contest.model.User
import com.example.digital_contest.R
import com.example.digital_contest.model.db.Board.BoardDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val authDB : AuthDB = AuthDB()
val boardDB : BoardDB = BoardDB()

lateinit var temp_userData : User


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        CoroutineScope(Dispatchers.IO).launch {
            delay(3000)
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}