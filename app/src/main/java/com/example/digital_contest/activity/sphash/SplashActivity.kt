package com.example.digital_contest.activity.sphash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.digital_contest.activity.login.LoginActivity
import com.example.digital_contest.model.db.Auth.AuthDB
import com.example.digital_contest.model.User
import com.example.digital_contest.R
import com.example.digital_contest.activity.main.MainActivity
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

        val currentUser = authDB.auth.currentUser
        if(currentUser != null){
            //이전에 사용자가 로그인한 경우 사용자 정보를 가져오고 mainActivity로 이동
            CoroutineScope(Dispatchers.IO).launch {
                val userData = authDB.getUserDataByEmail(currentUser.email.toString())!!

                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                intent.putExtra("userData", userData)
                startActivity(intent)
                finish()
            }
        }   else{
            //로그인하지 않은 경우 3초후에 LoginActivity로 이동
            CoroutineScope(Dispatchers.IO).launch {
                delay(3000)
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }   
        }
    }
}