package com.example.digital_contest.activity.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginActivityViewModel : ViewModel() {
    val id : MutableLiveData<String> = MutableLiveData("")
    val password : MutableLiveData<String> = MutableLiveData("")

    fun login(){
        Log.d("loginActivity", "sdad")
//        Log.d("loginActivity", id.value.toString())
//        Log.d("loginActivity", password.value.toString())
    }
}