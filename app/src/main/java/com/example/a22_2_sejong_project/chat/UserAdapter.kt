//package com.example.a22_2_sejong_project.chat
//
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentActivity
//import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.FragmentTransaction
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.a22_2_sejong_project.MainActivity
//import com.example.a22_2_sejong_project.R
//import com.example.a22_2_sejong_project.board.BoardDetailFragment
//import com.example.a22_2_sejong_project.board.BoardFragment
//import com.example.a22_2_sejong_project.databinding.FragmentUserBinding
//import de.hdodenhof.circleimageview.CircleImageView
//import kotlinx.android.synthetic.main.item_board_main.view.*
//
//class UserAdapter(private val context: UserFragment, private val userList:ArrayList<User>)
//    : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
//    class UserViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
//        val name: TextView = itemView.findViewById(R.id.user_name)
//        val note: TextView = itemView.findViewById(R.id.user_note)
//        val img: ImageView = itemView.findViewById(R.id.user_imageview)
//        val layoutUser:LinearLayout = itemView.findViewById(R.id.item_user)
//    }
//
//
//    //화면 설정
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
//
//        return UserViewHolder(view)
//    }
//    //데이터 설정
//    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
//        val user: User = userList[position]
//
//        holder.name.text = user.name
//        holder.note.text = user.note
//        Glide.with(context).load(user.profileImgUrl).error(R.drawable.user).fallback(R.drawable.user)
//            .circleCrop().into(holder.img)
//
//        holder.layoutUser.setOnClickListener{
//            val intent = Intent(context, MessageActivity::class.java)
//
//        }
//
////        holder.layoutUser.setOnClickListener {
////            val activity = it.context as AppCompatActivity
////
////            activity.supportFragmentManager
////                .beginTransaction()
////                .replace(R.id.fragment_user, ChatFragment())
////                .commit()
////            val bundle = Bundle()
////            bundle.putString("userId", user.uid)
////
////        }
//    }
//
//
//    //값 갯수 리턴
//    override fun getItemCount(): Int {
//        return userList.size
//    }
//}
//
//
