package com.example.digital_contest.activity.main.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.digital_contest.R
import com.example.digital_contest.BoardListAdapter
import com.example.digital_contest.activity.main.MainActivity
import com.example.digital_contest.activity.sphash.boardDB
import com.example.digital_contest.databinding.FragmentLikeListTabBinding
import kotlinx.coroutines.*

class LikeListFragment:Fragment(){
    lateinit var binding : FragmentLikeListTabBinding

    lateinit var job : Job

    override fun onCreateView(//view를 넣어주는 역할을
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_like_list_tab,container,false)
        val root = binding.root

        job = CoroutineScope(Dispatchers.IO).launch {
            val likeList = boardDB.getLikeBoards((activity as MainActivity).userData.likeBoardList)
            Log.d("likeList", likeList.toString())
            val likeListAdapter = BoardListAdapter(likeList, (activity as MainActivity).userData)
//            likeListAdapter.notifyDataSetChanged()

            withContext(Dispatchers.Main){
                binding.recyclerLikeList.adapter = likeListAdapter
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            job.join()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        job.cancel()
    }
}