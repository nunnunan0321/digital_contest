package com.example.digital_contest.activity.signUp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.digital_contest.R
import com.example.digital_contest.activity.signUp.SignUpActivity
import com.example.digital_contest.activity.signUp.SignUpActivityViewModel
import com.example.digital_contest.databinding.FragmentSignUp1Binding

class signUp1Fragment : Fragment() {
    lateinit var binding : FragmentSignUp1Binding
    lateinit var viewModel: SignUpActivityViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up1, container, false)
        viewModel = (activity as SignUpActivity).viewModel
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        val root = binding.root


        binding.btnSignUpNext.setOnClickListener{
            // 다음 버튼을 눌렀을때 처리하는 기능, 사용자 프로필 선택 화면과 사용자 메시지 입력 화면으로 이동한다.

            if(!viewModel.checkSamePassword()){
                Toast.makeText(requireContext(), "비밀번호가 같지 않습니다.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            (activity as SignUpActivity).navController.navigate(R.id.action_signUp1Fragment_to_signUp2Fragment)
        }

        viewModel.name.observe(requireActivity(), {
            binding.btnSignUpNext.isEnabled = viewModel.signUp1InputCheck()
        })

        viewModel.email.observe(requireActivity(), {
            binding.btnSignUpNext.isEnabled = viewModel.signUp1InputCheck()
        })

        viewModel.id.observe(requireActivity(), {
            binding.btnSignUpNext.isEnabled = viewModel.signUp1InputCheck()
        })

        viewModel.id.observe(requireActivity(), {
            binding.btnSignUpNext.isEnabled = viewModel.signUp1InputCheck()
        })

        viewModel.password.observe(requireActivity(), {
            binding.btnSignUpNext.isEnabled = viewModel.signUp1InputCheck()
        })

        viewModel.passwordRe.observe(requireActivity(), {
            binding.btnSignUpNext.isEnabled = viewModel.signUp1InputCheck()
        })

        return root
    }
}