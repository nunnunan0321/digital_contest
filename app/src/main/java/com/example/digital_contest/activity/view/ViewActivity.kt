package com.example.digital_contest.activity.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.digital_contest.R
import com.example.digital_contest.activity.write.WriteActivityViewModel
import com.example.digital_contest.databinding.ActivityViewBinding

class ViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityViewBinding
    lateinit var viewModel: WriteActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view)

        val boardId = intent.getStringExtra("boardId")

        binding.text.text = boardId
    }
}
