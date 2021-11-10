package com.example.digital_contest.activity.main.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class FragmentAdapter (fm : FragmentManager): FragmentPagerAdapter(fm) {
    //position 에 따라 원하는 Fragment로 이동시키기
    override fun getItem(position: Int): Fragment {
        val fragment =  when(position)
        {
            0-> MainFragment().newInstant() //맵 Fragment 가져오기
            1-> LikeListFragment().newInstant()
            2-> MoreMenuFragment().newInstant()
            else -> MainFragment().newInstant()
        }
        return fragment
    }

    //tab의 개수만큼 return
    override fun getCount(): Int = 3

    //tab의 이름 fragment마다 바꾸게 하기
    override fun getPageTitle(position: Int): CharSequence? {
        val title = when(position)
        {
            0->"main"
            1->"second"
            2->"third"
            else -> "main"
        }
        return title
    }
}