package com.example.digital_contest.activity.signUp

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.digital_contest.model.db.Auth.AuthResult
import com.example.digital_contest.R
import com.example.digital_contest.activity.login.LoginActivity
import com.example.digital_contest.activity.sphash.authDB
import com.example.digital_contest.databinding.ActivitySingUpBinding
import com.example.digital_contest.model.User
import kotlinx.coroutines.*

class SignUpActivity : AppCompatActivity() {
    lateinit var navController : NavController
    lateinit var viewModel: SignUpActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignUpActivityViewModel::class.java)
        setContentView(R.layout.activity_sing_up)


        navController = findNavController(R.id.fragment_signUp)
    }
}