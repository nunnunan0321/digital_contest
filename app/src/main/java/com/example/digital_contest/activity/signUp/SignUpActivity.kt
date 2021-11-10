package com.example.digital_contest.activity.signUp

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.digital_contest.model.db.Auth.AuthResult
import com.example.digital_contest.R
import com.example.digital_contest.activity.login.LoginActivity
import com.example.digital_contest.activity.sphash.authDB
import com.example.digital_contest.databinding.ActivitySingUpBinding
import com.example.digital_contest.model.User
import kotlinx.coroutines.*

class SignUpActivity : AppCompatActivity() {
    lateinit var binding : ActivitySingUpBinding
    lateinit var viewModel : SingUpActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sing_up)
        viewModel = ViewModelProvider(this).get(SingUpActivityViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        binding.btnSingUpSingUp.setOnClickListener{
            //로그인 버튼을 눌렀을때 처리하는 함수, id, email등을 가져오고 빈 값이 있는지 확인한뒤 로그인을 진행한다.

            val loadingDialog = Dialog(this@SignUpActivity)
            loadingDialog.setContentView(R.layout.dialog_loading)
            loadingDialog.show()

            val inputId = viewModel.id.value!!.toString()
            val inputEmail = viewModel.email.value!!.toString()
            val inputName = viewModel.name.value!!.toString()
            val inputPassword = viewModel.password.value!!.toString()

            val userData = User(id = inputId, name = inputName, email=inputEmail)

            CoroutineScope(Dispatchers.Main).launch {
                val authResult : AuthResult = CoroutineScope(Dispatchers.IO).async{
                    authDB.signUp(userData, inputPassword)
                }.await()

                if(authResult == AuthResult.OK){
                    //회원가입에 성공한 경우 LoginActivity로 이동한다.
                    val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }   else withContext(Dispatchers.Main){
                    //회원 가입에 실패한 경우
                    Toast.makeText(this@SignUpActivity, "회원가입에 실해했습니다.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}