package com.example.digital_contest.activity.signUp

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.digital_contest.activity.sphash.authDB
import com.example.digital_contest.model.User
import com.example.digital_contest.model.db.Auth.AuthResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SignUpActivityViewModel : ViewModel() {
    val id = MutableLiveData<String>("")
    val name = MutableLiveData<String>("")
    val email = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    val passwordRe = MutableLiveData<String>("")

    val userMSG = MutableLiveData("")
    val profileImg = MutableLiveData<Uri>()


    suspend fun signUp() : AuthResult {
        val userData = User(
            id = id.value.toString(),
            name = name.value.toString(),
            email = email.value.toString(),

            userMSG = userMSG.value.toString()
        )

        val result = authDB.signUp(userData, password.value.toString(), profileImg.value!!)
        Log.d("signResult, viewModel", result.toString())

        return result
    }
}