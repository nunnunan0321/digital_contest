package com.example.digital_contest.activity.write

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.CarrierConfigManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.digital_contest.R
import com.example.digital_contest.activity.sphash.boardDB
import com.example.digital_contest.activity.sphash.currentLocation
import com.example.digital_contest.databinding.ActivityWriteBinding
import com.example.digital_contest.model.Board
import com.example.digital_contest.model.User
import com.example.digital_contest.model.db.Board.BoardResult
import com.firebase.geofire.GeoLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.firebase.firestore.FieldValue
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
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(galleryIntent, 10)
        }
        binding.btnWriteWrite.setOnClickListener {
            saveBoard()
        }
    }

    fun saveBoard(){
        if(currentLocation == null){
            Toast.makeText(this, "위치 정보를 가져올 수 없습니다.", Toast.LENGTH_LONG).show()
            return
        }

        val boardData = Board(
            title = viewModel.title.value!!,
            writerID = viewModel.userData.value!!.id,
            contents = viewModel.content.value!!,
            uploadDate = FieldValue.serverTimestamp(),
            location = currentLocation!!
        )


        CoroutineScope(Dispatchers.Main).launch {
            val saveBoardResult : BoardResult = CoroutineScope(Dispatchers.IO).async {
                boardDB.saveBoard(boardData)
            }.await()

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