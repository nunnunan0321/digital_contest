package com.example.digital_contest

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.digital_contest.activity.main.MainActivity
import com.example.digital_contest.activity.sphash.authDB
import com.example.digital_contest.activity.view.ViewActivity
import com.example.digital_contest.databinding.LayoutRecyclerBoardListBinding
import com.example.digital_contest.databinding.LayoutRecyclerBoardListBindingImpl
import com.example.digital_contest.model.Board
import com.example.digital_contest.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat

class BoardListAdapter(val boardData : Map<String, Board>, val userData : User) : RecyclerView.Adapter<BoardListAdapter.Holder>(){
    val keys = boardData.keys.toList()
    val boards = boardData.values.toList()

    val usersData = mutableMapOf<String, User>()

    class Holder private constructor(val binding: LayoutRecyclerBoardListBinding) : RecyclerView.ViewHolder(binding.root){
        companion object{
            fun from(parent: ViewGroup) : Holder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LayoutRecyclerBoardListBinding.inflate(layoutInflater, parent, false)

                return Holder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder.from(parent)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        with(holder.binding){
            item.setOnClickListener {
                val intent = Intent(it.context, ViewActivity::class.java)
                intent.putExtra("boardId", keys[position])
                intent.putExtra("userData", userData)
                it.context.startActivity(intent)
            }

            val boardData = boards[position]

            title = boardData.title
            writer = boardData.writerID
            timeStamp = DateFormat.getDateInstance(DateFormat.MEDIUM).format(boardData.uploadDate)
            content = boardData.contents
            Glide.with(imgLikeListItemMainImg.context).load(boardData.imgUrl).into(imgLikeListItemMainImg)

            val writerID = boardData.writerID

            if(usersData.containsKey(writerID)){
                Glide.with(imgLikeListItemMainImg.context).load(usersData[writerID]!!.profileImgUrl).into(imgLikeListItemProfileImg)
            }   else{
                CoroutineScope(Dispatchers.IO).launch {
                    val writerData = authDB.getUserDataById(writerID)!!
                    withContext(Dispatchers.Main){
                        Glide.with(imgLikeListItemMainImg.context).load(writerData.profileImgUrl).into(imgLikeListItemProfileImg)
                    }
                    usersData[writerID] = writerData
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return boards.size
    }
}