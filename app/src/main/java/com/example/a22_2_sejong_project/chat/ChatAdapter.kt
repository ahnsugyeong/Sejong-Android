package com.example.a22_2_sejong_project.chat

import android.graphics.Color
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.a22_2_sejong_project.R

class ChatAdapter(val currentUser: String, val itemList: ArrayList<ChatLayout>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?

        if (viewType == 0){
            view = LayoutInflater.from(parent.context).inflate(R.layout.my_msgbox,parent,false)
            return MyChatViewHolder(view)
        }
        else{
            view = LayoutInflater.from(parent.context).inflate(R.layout.other_msgbox,parent,false)
            return OtherChatViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (currentUser == itemList[position].uid){
            holder as MyChatViewHolder
            holder.contents.text = itemList[position].contents
            holder.time.text = itemList[position].time
        }
        else {
            holder as OtherChatViewHolder
            holder.nickname.text = itemList[position].nickname
            holder.contents.text = itemList[position].contents
            holder.time.text = itemList[position].time
        }
    }

    override fun getItemViewType(position: Int): Int {
        return itemList[position].type
    }
    class MyChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val contents: TextView = itemView.findViewById(R.id.tv_msg)
        val time: TextView = itemView.findViewById(R.id.tv_time)
    }
    class OtherChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nickname: TextView = itemView.findViewById(R.id.tv_name)
        val contents: TextView = itemView.findViewById(R.id.tv_msg)
        val time: TextView = itemView.findViewById(R.id.tv_time)
    }

}