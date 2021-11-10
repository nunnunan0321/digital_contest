package com.example.digital_contest.activity.signUp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SingUpActivityViewModel : ViewModel() {
    var email = MutableLiveData("")
    val id = MutableLiveData("")
    val name = MutableLiveData("")
    val password = MutableLiveData("")
    val passwordRe = MutableLiveData("")
}