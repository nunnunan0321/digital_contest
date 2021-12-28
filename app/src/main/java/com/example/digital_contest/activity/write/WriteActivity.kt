package com.example.digital_contest.activity.write

import android.app.Dialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.digital_contest.R
import com.example.digital_contest.databinding.ActivityWriteBinding
import com.example.digital_contest.model.User
import com.example.digital_contest.model.db.Board.BoardResult
import com.example.digital_contest.model.db.together.TogetherResult
import kotlinx.coroutines.*
import java.util.*


class WriteActivity : AppCompatActivity() {
    lateinit var binding : ActivityWriteBinding
    lateinit var viewModel: WriteActivityViewModel

//    lateinit var writeMode : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_write)
        viewModel = ViewModelProvider(this).get(WriteActivityViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.rootDocumentId = intent.getStringExtra("rootDocumentId") ?: "normal"

        viewModel.userData.value = (intent.getSerializableExtra("userData") as User)

        binding.btnWriteImgChoice.setOnClickListener {
            getImageCallback.launch("image/*")
        }

        binding.btnWriteWrite.setOnClickListener {
            if(viewModel.img.value == null){
                Toast.makeText(this, "이미지를 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(viewModel.rootDocumentId == "normal") saveNormalBoard()
            else saveTogetherBoard()
        }

        binding.imgWritePrevBtn.setOnClickListener {
            finish()
        }


        viewModel.title.observe(this, {
            binding.btnWriteWrite.isEnabled = viewModel.writeInputEmptyCheck()
        })

        viewModel.content.observe(this, {
            binding.btnWriteWrite.isEnabled = viewModel.writeInputEmptyCheck()
        })
    }

    val getImageCallback = registerForActivityResult(ActivityResultContracts.GetContent()){
        try{
            val inputStream = contentResolver.openInputStream(it)
            val img = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            binding.imgWriteImagePreView.setImageBitmap(img)

            viewModel.img.value = it
        }   catch (e : Exception){}
    }

    fun saveNormalBoard(){
        val loadingDialog = Dialog(this)
        loadingDialog.setContentView(R.layout.dialog_loading)
        loadingDialog.setCancelable(false)
        loadingDialog.show()

        CoroutineScope(Dispatchers.Main).launch {
            val saveBoardResult : BoardResult = viewModel.saveBoard()

            loadingDialog.dismiss()

            if(saveBoardResult == BoardResult.OK){ //게시물 저장에 성공한 경우
                Toast.makeText(this@WriteActivity, "글쓰기 성공", Toast.LENGTH_LONG).show()
                finish()
            }
            else{ //게시뭉 저장에 실패한 경우
                Toast.makeText(this@WriteActivity, "글쓰기 실패", Toast.LENGTH_LONG).show()
            }
        }
    }


    fun saveTogetherBoard(){
        val loadingDialog = Dialog(this)
        loadingDialog.setContentView(R.layout.dialog_loading)
        loadingDialog.setCancelable(false)
        loadingDialog.show()

        CoroutineScope(Dispatchers.IO).launch {
            val saveBoardResult = viewModel.saveTogetherBoard()

            withContext(Dispatchers.Main){
                if(saveBoardResult == TogetherResult.OK){
                    Toast.makeText(this@WriteActivity, "게시물 저장에 성공했습니다.", Toast.LENGTH_LONG).show()
                    finish()
                }   else{
                    Toast.makeText(this@WriteActivity, "게시물 저장에 실패했습니다.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}