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
import kotlinx.coroutines.*
import java.util.*


class WriteActivity : AppCompatActivity() {
    lateinit var binding : ActivityWriteBinding
    lateinit var viewModel: WriteActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_write)

        viewModel = ViewModelProvider(this).get(WriteActivityViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.userData.value = (intent.getSerializableExtra("userData") as User)

        binding.btnWriteImgChoice.setOnClickListener {
            getImageCallback.launch("image/*")
        }
        binding.btnWriteWrite.setOnClickListener {
            saveBoard()
        }
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

    fun saveBoard(){
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
}