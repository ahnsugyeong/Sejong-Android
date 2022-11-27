package com.example.a22_2_sejong_project.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a22_2_sejong_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter(private val context: Context, private val chatList: ArrayList<Chat>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1
    var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == MESSAGE_TYPE_RIGHT) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.my_msgbox, parent, false)
            return MyChatViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.other_msgbox, parent, false)
            return OtherChatViewHolder(view)
        }

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    class MyChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val message: TextView = itemView.findViewById(R.id.tv_msg)
        val time: TextView = itemView.findViewById(R.id.tv_time)
    }
    class OtherChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val userName: TextView = itemView.findViewById(R.id.tv_name)
        val message: TextView = itemView.findViewById(R.id.tv_msg)
        val time: TextView = itemView.findViewById(R.id.tv_time)
        val profileImgUrl: ImageView = itemView.findViewById(R.id.iv)
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        if (chatList[position].senderId == firebaseUser!!.uid) {
            return MESSAGE_TYPE_RIGHT
        } else {
            return MESSAGE_TYPE_LEFT
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (chatList[position].senderId == firebaseUser!!.uid) {
            holder as MyChatViewHolder
            holder.message.text = chatList[position].message
            holder.time.text = chatList[position].time

        } else {
            holder as OtherChatViewHolder
            holder.userName.text = chatList[position].userName
            holder.message.text = chatList[position].message
            holder.time.text = chatList[position].time
            Glide.with(context).load(chatList[position].profileImgUrl)
                .error(R.drawable.user).fallback(R.drawable.user).circleCrop().into(holder.profileImgUrl)
        }
    }
}

//            var url: String? = null
//            FirebaseFirestore.getInstance().collection("user")
//                .whereEqualTo("uid",chatList[position].receiverId)
//                .get()
//                .addOnSuccessListener { documnets->
//                    for(doc in documnets){
//                        doc.get("profileUrl")?.let {
//                            url = it.toString()
//                        }
//                    }
//                }
//            Glide.with(context).load(url).error(R.drawable.user).fallback(R.drawable.user)
//                .circleCrop().into(holder.img)