package com.example.digital_contest.activity.login

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.digital_contest.activity.main.MainActivity
import com.example.digital_contest.R
import com.example.digital_contest.activity.signUp.SignUpActivity
import com.example.digital_contest.activity.sphash.authDB
import com.example.digital_contest.activity.sphash.temp_userData
import com.example.digital_contest.model.User
import com.example.digital_contest.databinding.ActivityLoginBinding
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding
    lateinit var viewModel : LoginActivityViewModel

    lateinit var id : String
    lateinit var password : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = LoginActivityViewModel()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initClickEvent()
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

            val loadingDialog = Dialog(this@LoginActivity)
            loadingDialog.setContentView(R.layout.dialog_loading)
            loadingDialog.show()

            CoroutineScope(Dispatchers.IO).launch {
                val loginResult : User? = authDB.login(id, password)

                loadingDialog.dismiss()

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