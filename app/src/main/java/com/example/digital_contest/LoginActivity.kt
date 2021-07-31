package com.example.digital_contest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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



    }
}