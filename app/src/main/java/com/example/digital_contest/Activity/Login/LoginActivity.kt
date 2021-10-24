package com.example.digital_contest.Activity.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.digital_contest.Activity.Main.MainActivity
import com.example.digital_contest.R
import com.example.digital_contest.Activity.SignUp.SignUpActivity
import com.example.digital_contest.Activity.Sphash.authDB
import com.example.digital_contest.Model.DB.Auth.AuthDB
import com.example.digital_contest.Model.User
import com.example.digital_contest.databinding.ActivityLoginBinding
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding

    lateinit var id : String
    lateinit var password : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        initClickEvent()
        
        
        //자동 로그인 코드
        val currentUser = authDB.auth.currentUser
        if(currentUser != null){
            CoroutineScope(Dispatchers.IO).launch {
                val userData = authDB.getUserDataByEmail(currentUser.email.toString())!!

                gotoMain(userData)
            }
        }
    }

    fun initClickEvent() = with(binding){
        txtLoginSingUp.setOnClickListener{
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        btnLoginLogin.setOnClickListener{
            id = edtLoginInputId.text.toString()
            password = edtLoginInputPassword.text.toString()

            if(loginInputEmptyCheck()) {return@setOnClickListener}

            CoroutineScope(Dispatchers.IO).launch {
                val loginResult : User? = authDB.login(id, password)

                if(loginResult == null){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@LoginActivity, "로그인에 실패했습니다.", Toast.LENGTH_LONG).show()
                    }
                    return@launch
                }

                gotoMain(loginResult)
            }
        }
    }

    fun loginInputEmptyCheck() : Boolean {
        if(id.isEmpty()){
            binding.edtLoginInputId.error = "ID를 입력하세요"
        }
        else if(password.isEmpty()){
            binding.edtLoginInputPassword.error = "비밀번호를 입력하세요."
        }
        else{
            return false
        }

        return true
    }

    fun gotoMain(userData : User){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("userData", userData)
        startActivity(intent)
        finish()
    }

}