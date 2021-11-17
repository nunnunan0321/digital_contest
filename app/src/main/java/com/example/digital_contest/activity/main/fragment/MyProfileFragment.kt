package com.example.digital_contest.activity.main.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.digital_contest.R
import com.example.digital_contest.activity.main.MainActivity
import com.example.digital_contest.databinding.FragmentMyProfileBinding

class MyProfileFragment : Fragment() {
    lateinit var binding : FragmentMyProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile, container, false)
        val root = binding.root

        binding.userData = (activity as MainActivity).userData

        return root
    }
}