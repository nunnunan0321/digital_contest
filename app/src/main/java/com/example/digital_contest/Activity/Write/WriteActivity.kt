package com.example.digital_contest.Activity.Write

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.digital_contest.R
import com.example.digital_contest.databinding.ActivityWriteBinding
import com.firebase.ui.auth.data.model.User

class WriteActivity : AppCompatActivity() {
    lateinit var binding : ActivityWriteBinding

    lateinit var userData : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_write)

        userData = intent.getSerializableExtra("user") as User
    }

    fun initClickEvent() = with(binding){

    }
}