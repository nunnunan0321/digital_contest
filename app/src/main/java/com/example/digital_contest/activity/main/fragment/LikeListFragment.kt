package com.example.digital_contest.activity.main.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.digital_contest.R
import com.example.digital_contest.activity.main.MainActivity
import com.example.digital_contest.activity.sphash.boardDB
import com.example.digital_contest.databinding.FragmentLikeListTabBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LikeListFragment:Fragment(){
    lateinit var binding : FragmentLikeListTabBinding

    override fun onCreateView(//view를 넣어주는 역할을
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_like_list_tab,container,false)
        val root = binding.root

        CoroutineScope(Dispatchers.IO).launch {
            val likeList = boardDB.getLikeBoards((activity as MainActivity).userData.likeBoardList)
            Log.d("likeList", likeList.toString())

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, likeList)
            withContext(Dispatchers.Main){
                binding.listLikeList.adapter = adapter
            }
        }

        return root
    }
}