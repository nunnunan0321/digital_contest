package com.example.digital_contest.Activity.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.digital_contest.Activity.Main.MainActivity
import com.example.digital_contest.R
import com.example.digital_contest.Activity.SignUp.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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


        // 자동 로그인
        // 현재 로그인된 계정을 가져온다. 가져온 계정이 있다면 이미 이전에 로그인이 된것이다.
        // 로그인 후 화면으로 이동시킨다.
        if(auth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            Toast.makeText(this, "어서오세요", Toast.LENGTH_LONG).show()

            startActivity(intent)
            finish()
        }

        singUp.setOnClickListener{
            var inputData = hashMapOf<String, String>()

            val intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra("inputData", inputData)
            startActivity(intent)
        }

        loginBtn.setOnClickListener{
            val id = inputId.text.toString()
            val password = inputPassword.text.toString()

            if(TextUtils.isEmpty(id)){
                inputId.setError("ID를 입력해주세요.")
                return@setOnClickListener
            }   else if(TextUtils.isEmpty(password)){
                inputPassword.setError("비밀번호를 입력해주세요")
                return@setOnClickListener
            }


            db.collection("user").document(id)
                .get()
                .addOnSuccessListener { document ->
                    val email = document.get("email").toString()

                    login(email, password)
                }
        }
    }

    fun login(email : String, password : String){
        // email과 password를 받아서 로그인을 진행하고 로그인에 성공했다면 MainActivity로 이동한다.
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    // 로그인에 성공했을때
                    Toast.makeText(this, "로그인에 성공했습니다. \n 어서오세요.", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }   else{
                    Toast.makeText(this, "로그인에 실패했습니다", Toast.LENGTH_LONG).show()
                }
            }
    } // end login()



    fun getEmailById(id : String) : String? {
        // id를 받아서 해당 id의 email을 가져온다. -> 폐기 예정
        var email : String? = null

        val documents = db.collection("user")
            .whereEqualTo("id", id)
            .get()
            .addOnSuccessListener { it ->
            for (document in it) {
                email = document.data["email"].toString()
                break
            }
        }

        return email
    }
} //end class