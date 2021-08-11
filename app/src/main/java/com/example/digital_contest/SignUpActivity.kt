package com.example.digital_contest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
    lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        //matching
        val inputId = findViewById<EditText>(R.id.edt_singUp_inputId)
        val inputName = findViewById<EditText>(R.id.edt_singUp_inputName)
        val inputEmail = findViewById<EditText>(R.id.edt_singUp_inputEmail)
        val inputPassword = findViewById<EditText>(R.id.edt_singUp_inputPassword)
        val inputPasswordRe = findViewById<EditText>(R.id.edt_singUp_inputPasswordRe)
        val singUpBtn = findViewById<Button>(R.id.btn_singUp_singUp)


        singUpBtn.setOnClickListener{
            if(TextUtils.isEmpty(inputId.text.toString())){ // inputId.text.toString()이 null인지 판단
                inputId.setError("ID를 입력헤주세요.")
                return@setOnClickListener//null이라면 에러 반환

            }   else if(TextUtils.isEmpty((inputEmail.text.toString()))){
                inputEmail.setError("이메일을 입력헤주세요.")
                return@setOnClickListener

            }   else if(TextUtils.isEmpty((inputPassword.text.toString()))){
                inputPassword.setError("비밀번호를 입력헤주세요.")
                return@setOnClickListener

            }   else if(TextUtils.isEmpty((inputPasswordRe.text.toString()))){
                inputPasswordRe.setError("비밀번호를 다시 입력헤주세요.")
                return@setOnClickListener

            }   else if(inputPassword.text.toString() != inputPasswordRe.text.toString()){
                inputPassword.setError("비밀번호가 일치하지 않습니다.")
                inputPasswordRe.setError("비밀번호가 일치하지 않습니다.")
                return@setOnClickListener

            }


            var userData = hashMapOf<String, Any>()
            userData["name"] = inputName.text.toString()
            userData["email"] = inputEmail.text.toString()
            userData["id"] = inputId.text.toString()

            auth.createUserWithEmailAndPassword(inputEmail.text.toString(), inputPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful){
                        db.collection("user")
                            .add(userData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "회원가입에 성공하였습니다.", Toast.LENGTH_LONG).show()
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener{
                                Toast.makeText(this, "회원가입에 실패하였습니다.", Toast.LENGTH_LONG).show()
                            }
                    }   else{
                        Toast.makeText(this, "회원가입에 실패하였습니다.", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}