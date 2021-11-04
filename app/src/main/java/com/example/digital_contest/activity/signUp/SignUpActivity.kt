package com.example.digital_contest.activity.signUp

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.digital_contest.model.db.Auth.AuthResult
import com.example.digital_contest.R
import com.example.digital_contest.activity.login.LoginActivity
import com.example.digital_contest.activity.sphash.authDB
import com.example.digital_contest.databinding.ActivitySingUpBinding
import com.example.digital_contest.model.User
import kotlinx.coroutines.*

class SignUpActivity : AppCompatActivity() {
    lateinit var binding : ActivitySingUpBinding

    lateinit var id : String
    lateinit var email : String
    lateinit var password : String
    lateinit var name : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sing_up)
        

        initClickEvent() //클릭이벤트 설정 함수
    }
    
    fun initClickEvent() = with(binding){
        // 클릭 이벤트 설정 함수
        btnSingUpSingUp.setOnClickListener{
            //로그인 버튼을 눌렀을때 처리하는 함수, id, email등을 가져오고 빈 값이 있는지 확인한뒤 로그인을 진행한다.

            val loadingDialog = Dialog(this@SignUpActivity)
            loadingDialog.setContentView(R.layout.dialog_loading)
            loadingDialog.show()

            id = edtSingUpInputId.text.toString()
            email = edtSingUpInputEmail.text.toString()
            password = edtSingUpInputPassword.text.toString()
            name = edtSingUpInputName.text.toString()

            if(signIpnputEmptyCheck()) { return@setOnClickListener }  //입력받을것중에 입력하지않은게 있는지 확인, 있다면 클릭 이벤트 종료

            val userData = User(id = id, name = name, email=email)

            CoroutineScope(Dispatchers.IO).launch {
                var result : AuthResult//? = null

                runBlocking {
                    result = authDB.signUp(userData, password)
                }

                withContext(Dispatchers.Main){
                    loadingDialog.dismiss()
                }

                if(result == AuthResult.OK){
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

    fun signIpnputEmptyCheck(): Boolean = with(binding){
        /*
        * 입력 받아야 하는 값중에 빈 값이 있는지 확인 한다. 있다면 True를 반환하고 없다면 False를 반환한다.
        */
        if(id.isEmpty()){ // inputId.text.toString()이 null인지.isEmpty()판단
            edtSingUpInputId.error = "ID를 입력헤주세요."
        }
        else if(name.isEmpty()) {
            edtSingUpInputName.error = "이름을 입력해주세요."
        }
        else if(email.isEmpty()){
            edtSingUpInputEmail.error = "이메일을 입력헤주세요."
        }
        else if(password.isEmpty()){
            edtSingUpInputPassword.error = "비밀번호를 입력헤주세요."
        }
        else if(edtSingUpInputPasswordRe.text.toString().isEmpty()){
            edtSingUpInputPasswordRe.error = "비밀번호를 다시 입력헤주세요."
        }
        else if(password != edtSingUpInputPasswordRe.text.toString()){
            edtSingUpInputPassword.error = "비밀번호가 일치하지 않습니다."
            edtSingUpInputPasswordRe.error = "비밀번호가 일치하지 않습니다."
        }
        else{
            return false
        }

        return false
    }
}