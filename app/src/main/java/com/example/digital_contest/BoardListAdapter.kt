package com.example.digital_contest

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.digital_contest.activity.main.MainActivity
import com.example.digital_contest.activity.sphash.authDB
import com.example.digital_contest.activity.view.ViewActivity
import com.example.digital_contest.databinding.LayoutRecyclerLikeListBinding
import com.example.digital_contest.model.Board
import com.example.digital_contest.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoardListAdapter(val boardData : Map<String, Board>, val userData : User) : RecyclerView.Adapter<BoardListAdapter.Holder>(){
    val keys = boardData.keys.toList()
    val boards = boardData.values.toList()

    val usersData = mutableMapOf<String, User>()

    class Holder private constructor(val binding: LayoutRecyclerLikeListBinding) : RecyclerView.ViewHolder(binding.root){
        companion object{
            fun from(parent: ViewGroup) : Holder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LayoutRecyclerLikeListBinding.inflate(layoutInflater, parent, false)

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

            title = boards[position].title
            writer = boards[position].writerID
            timeStamp = boards[position].uploadDate.toString()
            content = boards[position].contents
            Glide.with(imgLikeListItemMainImg.context).load(boards[position].imgUrl).into(imgLikeListItemMainImg)

            val writerID = boards[position].writerID

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