package com.example.digital_contest.activity.signUp.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.digital_contest.R
import com.example.digital_contest.activity.login.LoginActivity
import com.example.digital_contest.activity.signUp.SignUpActivity
import com.example.digital_contest.activity.signUp.SignUpActivityViewModel
import com.example.digital_contest.databinding.FragmentSignUp2Binding
import com.example.digital_contest.model.db.Auth.AuthResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class signUp2Fragment : Fragment() {
    lateinit var binding : FragmentSignUp2Binding
    lateinit var viewModel : SignUpActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up2, container, false)
        viewModel = (activity as SignUpActivity).viewModel
        binding.viewModel = viewModel
        val root = binding.root

        binding.imgSignUp2PrevBtn.setOnClickListener {
            (activity as SignUpActivity).navController.navigate(R.id.action_signUp2Fragment_to_signUp1Fragment)
        }

        binding.btnSignUp2Prev.setOnClickListener {
            (activity as SignUpActivity).navController.navigate(R.id.action_signUp2Fragment_to_signUp1Fragment)
        }

        binding.btnSignUp2ChoiceProfileImg.setOnClickListener {
            getProFileImageCallback.launch("image/*")
        }

        binding.btnSignUp2SignUp.setOnClickListener {
            val loadingDialog = Dialog(requireContext())
            loadingDialog.setContentView(R.layout.dialog_loading)
            loadingDialog.show()
            loadingDialog.setCancelable(false)

            CoroutineScope(Dispatchers.Main).launch {
                val signUpResult = viewModel.signUp()
                Log.d("signResult, fragment", signUpResult.toString())

                loadingDialog.dismiss()

                if(signUpResult == AuthResult.OK){
                    Toast.makeText(requireContext(), "회원가입에 성공했습니다.", Toast.LENGTH_LONG).show()
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    (activity as SignUpActivity).finish()
                }   else{
                    Toast.makeText(requireContext(), "회원가입에 실패했습니다.", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.userMSG.observe(requireActivity(), {
            binding.btnSignUp2SignUp.isEnabled = viewModel.signup2InputCheck()
        })



        return root
    }

    val getProFileImageCallback = registerForActivityResult(ActivityResultContracts.GetContent()){
        try{
            val inputStream = requireContext().contentResolver.openInputStream(it)
            val img = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            binding.imgSignUp2ProfileImgPreView.setImageBitmap(img)
            viewModel.profileImg.value = it

        }   catch (e : Exception){}
    }
}