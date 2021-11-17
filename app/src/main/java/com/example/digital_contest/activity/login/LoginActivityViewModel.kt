package com.example.digital_contest.activity.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.digital_contest.activity.sphash.authDB
import com.example.digital_contest.model.User

class LoginActivityViewModel(application : Application) : AndroidViewModel(application) {
    val id = MutableLiveData("")
    val password  = MutableLiveData("")
    var check = 3

    fun loginInputCheck() : Boolean{
        if(id.value!!.isEmpty()) return false
        if(password.value!!.isEmpty()) return false

        return true
    }

    suspend fun login() : User? {
        return authDB.login(id.value.toString(), password.value.toString())
    }
}