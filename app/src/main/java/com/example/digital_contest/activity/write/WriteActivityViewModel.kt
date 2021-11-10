package com.example.digital_contest.activity.write

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.digital_contest.model.User

class WriteActivityViewModel : ViewModel() {
    val userData = MutableLiveData<User>()
    val title = MutableLiveData("")
    val content = MutableLiveData("")
}