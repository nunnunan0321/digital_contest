package com.example.digital_contest

import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.TextureView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
    lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

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


            db.collection("user")
                .whereEqualTo("id", inputId.text.toString())
                .get()
                .addOnSuccessListener { documents ->
                    var email = ""
                    for(document in documents){
                        email = document.data["email"].toString()
                    }

                    auth.signInWithEmailAndPassword(email, inputPassword.text.toString())
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
                .addOnFailureListener{
                    Toast.makeText(this, "로그인에 실패했습니다.", Toast.LENGTH_LONG).show()
                }



        }

    }
}