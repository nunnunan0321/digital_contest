package com.example.digital_contest.Activity.Main

import android.Manifest
import android.content.ContentValues.TAG
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.ViewPager
import com.example.digital_contest.Activity.Main.Fragment.FragmentAdapter
import com.example.digital_contest.Model.User
import com.example.digital_contest.R
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    lateinit var userData : User

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userData = intent.getSerializableExtra("userData") as User
//        Log.d("userData Mainactivity", userData.toString())


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


        val pagerAdapter = FragmentAdapter(supportFragmentManager)
        val pager:ViewPager=findViewById(R.id.viewPager)
        pager.adapter = pagerAdapter
        pager.setPageTransformer(true, ZoomOutPageTransformer()) //화면 이동시 화면 축소
        val tab:TabLayout=findViewById(R.id.tab)
        tab.setupWithViewPager(pager)
    }
}
