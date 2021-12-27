package com.example.digital_contest.activity.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.digital_contest.R
import com.example.digital_contest.activity.sphash.authDB
import com.example.digital_contest.model.Comment
import com.example.digital_contest.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat

class CommentListAdapter(val items : MutableList<Comment>) : RecyclerView.Adapter<CommentListAdapter.CommentListHolder>() {
    val userList = mutableMapOf<String, User>()

    class CommentListHolder(val view : View) : RecyclerView.ViewHolder(view) {
        val userID = view.findViewById<TextView>(R.id.txt_commentListItem_id)
        val content = view.findViewById<TextView>(R.id.txt_commentListItem_content)
        val date = view.findViewById<TextView>(R.id.txt_commentListItem_date)
        val profileImg = view.findViewById<ImageView>(R.id.img_commentListItem_profileImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentListHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.layout_comment_list_item, parent, false)

        return CommentListHolder(layout)
    }

    override fun onBindViewHolder(holder: CommentListHolder, position: Int) {
        val comment = items[position]
        with(holder){
            userID.text = comment.writerID
            content.text = comment.content
            date.text = DateFormat.getDateInstance(DateFormat.MEDIUM).format(comment.timeStamp)

            if(!userList.containsKey(comment.writerID)){
                CoroutineScope(Dispatchers.IO).launch{
                    userList[comment.writerID] = authDB.getUserDataById(comment.writerID)!!

                    withContext(Dispatchers.Main){
                        Glide.with(profileImg.context).load(userList[comment.writerID]!!.profileImgUrl).into(profileImg)
                    }
                }
            }   else{
                Glide.with(profileImg.context).load(userList[comment.writerID]).into(profileImg)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}