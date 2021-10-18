package com.example.digital_contest.Activity.SignUp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.digital_contest.R
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
            val id = inputId.text.toString()
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()
            val name = inputName.text.toString()

            if(TextUtils.isEmpty(id)){ // inputId.text.toString()이 null인지 판단
                inputId.error = "ID를 입력헤주세요."
                return@setOnClickListener//null이라면 에러 반환

            }   else if(TextUtils.isEmpty(name)) {
                inputName.error = "이름을 입력해주세요."
                return@setOnClickListener
            }   else if(TextUtils.isEmpty(email)){
                inputEmail.error = "이메일을 입력헤주세요."
                return@setOnClickListener

            }   else if(TextUtils.isEmpty(password)){
                inputPassword.error = "비밀번호를 입력헤주세요."
                return@setOnClickListener

            }   else if(TextUtils.isEmpty((inputPasswordRe.text.toString()))){
                inputPasswordRe.error = "비밀번호를 다시 입력헤주세요."
                return@setOnClickListener

            }   else if(password != inputPasswordRe.text.toString()){
                inputPassword.error = "비밀번호가 일치하지 않습니다."
                inputPasswordRe.error = "비밀번호가 일치하지 않습니다."
                return@setOnClickListener

            }




            //Log.d("SingUp", singUp(inputEmail.text.toString(), inputPassword.text.toString()).toString())
            if(saveUserData(name, id, email)){
                if(singUp(email, password)){ //로그인을 진행한다.
                    Toast.makeText(this, "회원가입에 성공했습니다.", Toast.LENGTH_LONG).show()
                    finish()
                }   else{
                    Toast.makeText(this, "회원가입에 실패했습니다.", Toast.LENGTH_LONG).show()
                }
            }   else{
                Toast.makeText(this, "회원가입에 실패했습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun saveUserData(name : String, id : String, email : String) : Boolean{
        // 사용자의 정보(이름, id, password)를 데이터베이스에 저장하고 저장 결과를 반환합니다.

        var userData = hashMapOf<String, Any>(
            "name" to name,
//            "id" to id,
            "email" to email,
        )

        db.collection("user").document(id)
            .set(userData)
            .addOnSuccessListener(
                return true
            )

        return false
    }

    fun singUp(email : String, password : String ) : Boolean{
        // email과 password를 받아서 회원가입을 진행합니다.
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener (
                return true
            )

        return false
    }
}