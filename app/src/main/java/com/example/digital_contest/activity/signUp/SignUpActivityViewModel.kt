package com.example.digital_contest.activity.signUp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.digital_contest.model.User

class SignUpActivityViewModel : ViewModel() {
    val id = MutableLiveData<String>("")
    val name = MutableLiveData<String>("")
    val email = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    val passwordRe = MutableLiveData<String>("")

    val userMSG = MutableLiveData("")
}