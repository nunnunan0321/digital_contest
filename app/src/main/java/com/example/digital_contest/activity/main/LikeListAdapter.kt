package com.example.digital_contest.activity.main

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.digital_contest.activity.view.ViewActivity
import com.example.digital_contest.databinding.LayoutRecyclerLikeListBinding
import com.example.digital_contest.model.Board

class LikeListAdapter(val boardData : Map<String, Board>) : RecyclerView.Adapter<LikeListAdapter.Holder>(){
    val keys = boardData.keys.toList()
    val boards = boardData.values.toList()


//    init {
//        with(boards){
//            Collection
//        }
//    }

    class Holder private constructor(val binding: LayoutRecyclerLikeListBinding) : RecyclerView.ViewHolder(binding.root){
        companion object{
            fun from(parent: ViewGroup) : Holder{
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
                intent.putExtra("userData", (it.context as MainActivity).userData)
                it.context.startActivity(intent)
            }
            title = boards[position].title
            writer = boards[position].writerID
            timeStamp = boards[position].uploadDate.toString()
            content = boards[position].contents

            Glide.with(imgLikeListItemMainImg.context).load(boards[position].imgUrl).into(imgLikeListItemMainImg)
        }
    }

    override fun getItemCount(): Int {
        return boards.size
    }
}