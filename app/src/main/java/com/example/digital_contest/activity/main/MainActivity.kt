package com.example.digital_contest.activity.main

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.digital_contest.activity.write.WriteActivity
import com.example.digital_contest.model.User
import com.example.digital_contest.R
import com.example.digital_contest.activity.sphash.authDB
import com.example.digital_contest.activity.sphash.boardDB
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var userData : User

    lateinit var navControl : NavController

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userData = intent.getSerializableExtra("userData") as User
        Toast.makeText(this, "어서오세요. ${userData.name}님", Toast.LENGTH_LONG).show()

        Log.d("userData", userData.toString())

        navControl = findNavController(R.id.fragment_main)
        findViewById<BottomNavigationView>(R.id.bottomNav_main).setupWithNavController(navControl)


        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    Log.d(TAG, "정확한 위치 권한 허용됨")
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    Log.d(TAG, "대략적인 위치 권한 허용됨")
                } else -> {
                Log.d(TAG, "위치 권한 거부됨")
                Toast.makeText(this, "위치 권한이 거부되었습니다. 앱을 재실행해주세요.", Toast.LENGTH_SHORT).show()
            }
            }
        }
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    override fun onRestart() {
        super.onRestart()

        CoroutineScope(Dispatchers.IO).launch {
            userData = authDB.getUserDataById(userData.id)!!
        }
    }
}
