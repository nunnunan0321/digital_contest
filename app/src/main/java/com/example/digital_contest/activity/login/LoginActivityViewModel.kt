package com.example.digital_contest.activity.login

import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.digital_contest.activity.main.MainActivity
import com.example.digital_contest.activity.sphash.authDB
import com.example.digital_contest.model.User
import com.example.digital_contest.model.db.Auth.AuthResult
import com.firebase.ui.auth.AuthUI.getApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginActivityViewModel(application : Application) : AndroidViewModel(application) {
    val my_application = application
    val id = MutableLiveData("")
    val password  = MutableLiveData("")

    fun login(){

    }
}