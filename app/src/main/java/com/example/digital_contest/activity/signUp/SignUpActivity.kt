package com.example.digital_contest.activity.signUp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.digital_contest.R

class SignUpActivity : AppCompatActivity() {
    lateinit var navController : NavController
    lateinit var viewModel: SignUpActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignUpActivityViewModel::class.java)
        setContentView(R.layout.activity_sign_up)


        navController = findNavController(R.id.fragment_signUp)
    }
}