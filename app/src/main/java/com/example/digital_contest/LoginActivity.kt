package com.example.digital_contest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.TextureView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        //matching
        val inputId = findViewById<EditText>(R.id.edt_login_inputId)
        val inputPassword = findViewById<EditText>(R.id.edt_login_inputPassword)
        val loginBtn = findViewById<Button>(R.id.btn_login_login)
        val singUp = findViewById<TextView>(R.id.txt_login_singUp)

        singUp.setOnClickListener{
            val intent = Intent(this, SingUpActivity::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener{
            if(TextUtils.isEmpty(inputId.text.toString())){
                inputId.setError("ID를 입력해주세요.")
                return@setOnClickListener
            }   else if(TextUtils.isEmpty(inputPassword.text.toString())){
                inputPassword.setError("비밀번호를 입력해주세요")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(inputId.text.toString(), inputPassword.text.toString())
                .addOnCompleteListener(this){task ->
                    if(task.isSuccessful){
                        //로그인에 성공했을때
                        Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }   else{
                        //로그인에 실패했을때
                        Toast.makeText(this, "로그인에 실패했습니다.", Toast.LENGTH_LONG).show()
                    }
                }
        }

    }
}