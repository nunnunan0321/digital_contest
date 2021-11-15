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

    fun checkSamePassword() : Boolean{
        //사용자가 입력한 비밀번호가 서로 다른지 확인
        if(password.value != passwordRe.value) return false

        return true
    }

    fun signUp1InputCheck() : Boolean {
        // 사용자의 입력이 비어있거나 잘못되었는지 판단하는 함수

        if(id.value!!.isEmpty()) return false
        if(name.value!!.isEmpty()) return false
        if(email.value!!.isEmpty()) return false
        if(password.value!!.isEmpty()) return false
        if(passwordRe.value!!.isEmpty()) return false
        if(password.value!!.length < 8) return false


        return true
    }

    fun signup2InputCheck() : Boolean {
        if(userMSG.value!!.isEmpty()) return false

        return true
    }

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