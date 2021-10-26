package com.example.digital_contest.activity.main.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.digital_contest.activity.main.MainActivity
import com.example.digital_contest.model.User
import com.example.digital_contest.R
import com.example.digital_contest.activity.login.LoginActivity
import com.example.digital_contest.activity.sphash.authDB
import com.example.digital_contest.activity.write.WriteActivity
import com.example.digital_contest.databinding.FragmentMoreMenuTabBinding


class MoreMenuFragment:Fragment(){
    lateinit var binding : FragmentMoreMenuTabBinding
    lateinit var userData : User

    override fun onCreateView(//view를 넣어주는 역할을
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more_menu_tab,container,false)
        val root = binding.root

        userData = (activity as MainActivity).userData

        initClickEvent()

        return root
    }
    fun newInstant() : MoreMenuFragment
    {
        val args = Bundle()
        val frag = MoreMenuFragment()
        frag.arguments = args
        return frag
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        class MainActivity : AppCompatActivity()

    }

    fun initClickEvent() = with(binding){
        btnThirdTapLogout.setOnClickListener {
            authDB.auth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)

            //프래그먼트에서의 finish() 와 같다함
            activity?.finish()
        }

        btnThirdTapWrite.setOnClickListener {
//            (activity as MainActivity).gotoWrite()
            val intent = Intent(requireContext(), WriteActivity::class.java)
            intent.putExtra("userData", userData)
            startActivity(intent)
        }

        btnThirdTapGetUserData.setOnClickListener {
            Log.d("ThirdTap", userData.toString())
        }
    }
}