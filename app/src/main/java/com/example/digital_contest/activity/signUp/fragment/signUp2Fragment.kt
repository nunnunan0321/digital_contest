package com.example.digital_contest.activity.signUp.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.digital_contest.R
import com.example.digital_contest.activity.login.LoginActivity
import com.example.digital_contest.activity.signUp.SignUpActivity
import com.example.digital_contest.activity.signUp.SignUpActivityViewModel
import com.example.digital_contest.activity.sphash.authDB
import com.example.digital_contest.databinding.FragmentSingUp2Binding
import com.example.digital_contest.model.User
import com.example.digital_contest.model.db.Auth.AuthResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class singUp2Fragment : Fragment() {
    lateinit var binding : FragmentSingUp2Binding
    lateinit var viewModel : SignUpActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sing_up2, container, false)
        viewModel = (activity as SignUpActivity).viewModel
        binding.viewModel = viewModel
        val root = binding.root

        binding.btnSignUp2SignUp.setOnClickListener {
            var userData : User
            with(viewModel){
                userData = User(
                    id = id.value.toString(),
                    email = email.value.toString(),
                    name = name.value.toString(),
                    userMSG =  userMSG.value.toString()
                )
            }

            val loadingDialog = Dialog(requireContext())
            loadingDialog.setContentView(R.layout.dialog_loading)
            loadingDialog.show()

            CoroutineScope(Dispatchers.Main).launch {
                val signUpResult : AuthResult = CoroutineScope(Dispatchers.IO).async {
                    authDB.signUp(userData, viewModel.password.value.toString())
                }.await()

                loadingDialog.dismiss()

                if(signUpResult == AuthResult.OK){
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    (activity as SignUpActivity).finish()
                }
            }
        }


        return root
    }
}