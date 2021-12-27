package com.example.digital_contest.activity.main.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.digital_contest.BoardListAdapter
import com.example.digital_contest.R
import com.example.digital_contest.activity.boardList.BoardListActivity
import com.example.digital_contest.activity.login.LoginActivity
import com.example.digital_contest.activity.main.MainActivity
import com.example.digital_contest.activity.sphash.authDB
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
        Glide.with(this).load((activity as MainActivity).userData.profileImgUrl).into(binding.imgLikeListItemProfileImg)

        binding.layoutLike.setOnClickListener{
            (activity as MainActivity).navControl.navigate(R.id.action_MyProfileFragment_to_LikeListFragment)
        }

        binding.layoutMyProfileWritten.setOnClickListener{
            val intent = Intent(requireContext(), BoardListActivity::class.java)
            intent.putExtra("userData", (activity as MainActivity).userData)
            (activity as MainActivity).startActivity(intent)
        }

        binding.layoutLogout.setOnClickListener {
            authDB.auth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return root
    }
}