package com.example.digital_contest.Activity.Main.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.digital_contest.Activity.Login.LoginActivity
import com.example.digital_contest.Activity.Sphash.authDB
import com.example.digital_contest.Activity.Write.WriteActivity
import com.example.digital_contest.Model.User
import com.example.digital_contest.R
import com.example.digital_contest.databinding.FragmentThirdTabBinding
import kotlinx.coroutines.*

class ThirdTabFragment:Fragment(){
    lateinit var binding : FragmentThirdTabBinding

    override fun onCreateView(//view를 넣어주는 역할을
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_third_tab,container,false)
        val root = binding.root

        val current_user = authDB.auth.currentUser!!




        initClickEvent()

        return root
    }

    fun newInstant() : ThirdTabFragment {
        val args = Bundle()
        val frag = ThirdTabFragment()
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
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.remove(this@ThirdTabFragment)
                ?.commit()
        }


        btnThirdTapWrite.setOnClickListener {
            val intent = Intent(activity, WriteActivity::class.java)
//            Log.d("userData", userData.toString())
//            intent.putExtra("userData", userData)
            startActivity(intent)
        }

        btnThirdTapGetUserData.setOnClickListener{
//            Toast.makeText(requireContext(), "${userData.id}, ${userData.name}", Toast.LENGTH_LONG).show()
        }
    }
}