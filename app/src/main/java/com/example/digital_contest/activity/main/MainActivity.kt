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
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.digital_contest.activity.write.WriteActivity
import com.example.digital_contest.model.User
import com.example.digital_contest.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var userData : User

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userData = intent.getSerializableExtra("userData") as User
//        temp_userData = userData
        Toast.makeText(this, "어서오세요. ${userData.name}님", Toast.LENGTH_LONG).show()

        val navControl = findNavController(R.id.fragment_main)
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
}
