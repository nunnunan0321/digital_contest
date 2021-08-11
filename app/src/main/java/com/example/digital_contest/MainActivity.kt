package com.example.digital_contest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
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

