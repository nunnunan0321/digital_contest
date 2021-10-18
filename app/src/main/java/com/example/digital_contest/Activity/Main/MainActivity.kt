package com.example.digital_contest.Activity.Main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.digital_contest.Activity.Main.Fragment.FragmentAdapter
import com.example.digital_contest.R
import com.example.digital_contest.Activity.Main.Fragment.ZoomOutPageTransformer
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pagerAdapter = FragmentAdapter(supportFragmentManager)
        val pager:ViewPager=findViewById(R.id.viewPager)
        pager.adapter = pagerAdapter
        pager.setPageTransformer(true, ZoomOutPageTransformer()) //화면 이동시 화면 축소
        val tab:TabLayout=findViewById(R.id.tab)
        tab.setupWithViewPager(pager)


    }
}

