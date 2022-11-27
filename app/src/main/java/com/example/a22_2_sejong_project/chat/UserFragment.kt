package com.example.a22_2_sejong_project.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.R
import com.bumptech.glide.Glide
import com.example.a22_2_sejong_project.MainActivity
import com.example.a22_2_sejong_project.databinding.FragmentUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.protobuf.Value
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UserFragment: Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter: UserAdapter
    lateinit var userList: ArrayList<User>
    private var currentUser : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding.root

        userList = ArrayList()

        adapter = UserAdapter()

        binding.recyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.recyclerview.adapter = adapter

        currentUser = Firebase.auth.currentUser?.toString()
        

        FirebaseFirestore.getInstance().collection("user").get().addOnSuccessListener { result ->
            userList.clear()
            for (document in result) {
                val name = document.data.get("nickname").toString()
                val note = document.data.get("note").toString()
                val img = document.data.get("profileUrl").toString()
                val uid = document.data.get("uid").toString()

                val item: User
                item = User(name, img, note, uid)
                userList.add(item)

            }
            adapter.notifyDataSetChanged()
        }


        return view
    }

    inner class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
        inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val name: TextView =
                itemView.findViewById(com.example.a22_2_sejong_project.R.id.user_name)
            val note: TextView =
                itemView.findViewById(com.example.a22_2_sejong_project.R.id.user_note)
            val img: ImageView =
                itemView.findViewById(com.example.a22_2_sejong_project.R.id.user_imageview)
            val layoutUser: LinearLayout =
                itemView.findViewById(com.example.a22_2_sejong_project.R.id.item_user)
        }


        //화면 설정
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(com.example.a22_2_sejong_project.R.layout.item_user, parent, false)

            return UserViewHolder(view)
        }

        //데이터 설정
        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
            val user: User = userList[position]
            var destinationUid: String? = null
            val destinationUsers : ArrayList<String> = arrayListOf()

            for (users in userList[position].uid.toString()){
                if (!users.equals(currentUser)){
                    destinationUid = users.toString()
                    destinationUsers.add(destinationUid)
                }
            }

            holder.name.text = user.name
            holder.note.text = user.note
            Glide.with(holder.itemView.context).load(user.profileImgUrl)
                .error(com.example.a22_2_sejong_project.R.drawable.user).fallback(
                com.example.a22_2_sejong_project.R.drawable.user
            )
                .circleCrop().into(holder.img)

//        holder.layoutUser.setOnClickListener {
//            val activity = it.context as AppCompatActivity
//
//            activity.supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.fragment_user, ChatFragment())
//                .commit()
//            val bundle = Bundle()
//            bundle.putString("userId", user.uid)
//
//        }
            holder.layoutUser.setOnClickListener {
                val intent = Intent(context, MessageActivity::class.java)
                intent.putExtra("uid",user.uid)
                intent.putExtra("userName",user.name)
                intent.putExtra("profileImgUrl", user.profileImgUrl)
                context?.startActivity(intent)
            }
        }

        //값 갯수 리턴
        override fun getItemCount(): Int {
            return userList.size
        }


    }

}

