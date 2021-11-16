package com.example.digital_contest.activity.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.digital_contest.R
import com.example.digital_contest.model.Board

class LikeListAdapter(val items : List<Board>) : RecyclerView.Adapter<LikeListAdapter.Holder>(){
    class Holder(val view : View) : RecyclerView.ViewHolder(view){
        val title = view.findViewById<TextView>(R.id.txt_likeListItem_title)
        val writer = view.findViewById<TextView>(R.id.txt_likeListItem_writer)
        val timeStamp = view.findViewById<TextView>(R.id.txt_likeListItem_timeStamp)
        val content = view.findViewById<TextView>(R.id.txt_likeListItem_content)
        val userProfileImg = view.findViewById<ImageView>(R.id.img_likeListItem_profileImg)
        val contentImg = view.findViewById<ImageView>(R.id.img_likeListItem_mainImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.layout_recycler_like_list, parent, false)

        return Holder(layout)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        with(holder){
            title.text = items[position].title
            writer.text = items[position].writerID
            timeStamp.text = items[position].uploadDate.toString()
            content.text = items[position].contents
//            contentImg
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}