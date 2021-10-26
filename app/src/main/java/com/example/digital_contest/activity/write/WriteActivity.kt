package com.example.digital_contest.activity.write

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.digital_contest.R
import com.example.digital_contest.activity.sphash.boardDB
import com.example.digital_contest.activity.sphash.temp_userData
import com.example.digital_contest.databinding.ActivityWriteBinding
import com.example.digital_contest.model.Board
import com.example.digital_contest.model.User
import com.example.digital_contest.model.db.Board.BoardResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WriteActivity : AppCompatActivity() {
    lateinit var binding : ActivityWriteBinding
//    lateinit var viewModel: WriteActivityViewModel
    lateinit var userData : User

    lateinit var writerID : String
    lateinit var contents : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_write)
        userData = temp_userData
        writerID = userData.id

        //MVVM코드
        /*
        viewModel = WriteActivityViewModel()
        viewModel.userData = temp_userData
        binding.viewModel = viewModel
         */

        initClickEvent()
    }

    fun initClickEvent() = with(binding){
        btnWriteWrite.setOnClickListener {
            contents = edtWriteContents.text.toString()
            Log.d("writeActivity", "클릭됨")

            if(saveBoardEmptyCheck()) {return@setOnClickListener}

            val boardData = Board(writerID = writerID, contents = contents)
            Log.d("writeActivity", boardData.toString())

            CoroutineScope(Dispatchers.IO).launch {
                val saveBoardResult = boardDB.saveBoard(boardData)

                if(saveBoardResult == BoardResult.OK){
                    //게시물 저장에 성공한 경우
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@WriteActivity, "글쓰기 성공", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
                else{
                    //게시뭉 저장에 실패한 경우
                    Toast.makeText(this@WriteActivity, "글쓰기 실패", Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    fun saveBoardEmptyCheck() : Boolean{
        if(contents.isEmpty()){
            binding.edtWriteContents.error = "내용을 입력해주세요."
        }   else{
            return false
        }

        return true
    }
}