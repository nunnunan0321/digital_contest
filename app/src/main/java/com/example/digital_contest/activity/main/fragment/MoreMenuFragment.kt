package com.example.digital_contest.activity.main.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.digital_contest.model.User
import com.example.digital_contest.R
import com.example.digital_contest.activity.login.LoginActivity
import com.example.digital_contest.activity.main.MainActivity
import com.example.digital_contest.activity.sphash.authDB
import com.example.digital_contest.activity.sphash.boardDB
import com.example.digital_contest.databinding.FragmentMoreMenuTabBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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

        CoroutineScope(Dispatchers.IO).launch {
            val boards = boardDB.getAllBoard()

            withContext(Dispatchers.Main){
//                Log.d("boards", boards.values.toString())
//                binding.listMore.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, )
            }
        }

        return root
    }
    fun newInstant() : MoreMenuFragment {
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
            activity?.finish()
        }

        btnThirdTapGetUserData.setOnClickListener {
            Toast.makeText(activity, userData.toString(), Toast.LENGTH_LONG).show()
        }
    }
}