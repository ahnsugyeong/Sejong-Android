package com.example.a22_2_sejong_project.chat

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.a22_2_sejong_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.item_user.*
import kotlinx.android.synthetic.main.other_msgbox.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MessageActivity: AppCompatActivity() {

    private var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    var chatList = ArrayList<Chat>()
    var topic = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat) //수정

        rv_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        var intent = intent
        var uid = intent.getStringExtra("uid")
        var receiverName = intent.getStringExtra("userName")
        var img = intent.getStringExtra("profileImgUrl")

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users").child(uid!!)

        imgBack.setOnClickListener {
            onBackPressed()
        }


        et_chatting.addTextChangedListener { text ->
            btn_send.isEnabled = text.toString() != ""
        }
        var senderName:String? = null
        FirebaseFirestore.getInstance().collection("user")
            .whereEqualTo("uid",firebaseUser!!.uid)
            .get()
            .addOnSuccessListener { documnets->
                for(doc in documnets){
                    doc.get("nickname")?.let {
                        senderName = it.toString()
                    }
                }
            }

        top_name.text = receiverName
        btn_send.setOnClickListener {
            var message: String = et_chatting.text.toString()

            if (message.isEmpty()) {
                Toast.makeText(applicationContext, "message is empty", Toast.LENGTH_SHORT).show()
                et_chatting.setText("")
            } else {
                val sf = SimpleDateFormat("a hh:mm",Locale.KOREA)
                sf.timeZone = TimeZone.getTimeZone("Asia/Seoul")
                val time = sf.format(System.currentTimeMillis())
                sendMessage(firebaseUser!!.uid, uid, message, senderName!!, time, img)
                et_chatting.setText("")
                topic = "/topic/$uid"
                //알림
//                PushNotification(NotificationData( name!!,message),
//                    topic).also {
//                    sendNotification(it)
//                }

            }
        }
        readMessage(firebaseUser!!.uid, uid)
    }

    private fun sendMessage(senderId: String, receiverId: String, message: String, senderName: String, time: String, img: String?) {
        var reference: DatabaseReference? = FirebaseDatabase.getInstance().getReference()

        var hashMap: HashMap<String, String> = HashMap()
        hashMap.put("senderId", senderId)
        hashMap.put("receiverId", receiverId)
        hashMap.put("message", message)
        hashMap.put("senderName", senderName)
        hashMap.put("time", time)
        hashMap.put("profileImgUrl", img!!)

        reference!!.child("Chat").push().setValue(hashMap)
    }
    fun readMessage(senderId: String, receiverId: String) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(Chat::class.java)

                    if (chat!!.senderId.equals(senderId) && chat!!.receiverId.equals(receiverId) ||
                        chat!!.senderId.equals(receiverId) && chat!!.receiverId.equals(senderId)
                    ) {
                        chatList.add(chat)
                    }
                }

                val chatAdapter = ChatAdapter(this@MessageActivity, chatList)

                rv_list.adapter = chatAdapter
                rv_list.scrollToPosition(chatList.size-1)
            }
        })
    }
}
//
//
//import android.annotation.SuppressLint
//import android.os.Bundle
//import android.os.Handler
//import android.util.Log
//import android.view.Gravity
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.example.a22_2_sejong_project.R
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//import com.google.firebase.database.ktx.getValue
//import com.google.firebase.ktx.Firebase
//import kotlinx.android.synthetic.main.fragment_chat.*
//import java.text.SimpleDateFormat
//import java.util.*
//import kotlin.collections.ArrayList
//class MessageActivity : AppCompatActivity() {
//
//    private val fireDatabase = FirebaseDatabase.getInstance().reference
//    private var chatRoomUid : String? = null
//    private var destinationUid : String? = null
//    private var uid : String? = null
//    private var recyclerView : RecyclerView? = null
//
//    @SuppressLint("SimpleDateFormat")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_chat)
//        val imageView = findViewById<ImageView>(R.id.btn_send)
//        val editText = findViewById<TextView>(R.id.et_chatting)
//
//        //메세지를 보낸 시간
//        val time = System.currentTimeMillis()
//        val dateFormat = SimpleDateFormat("MM월dd일 hh:mm")
//        val curTime = dateFormat.format(Date(time)).toString()
//
//        destinationUid = intent.getStringExtra("destinationUid")
//        uid = Firebase.auth.currentUser?.uid.toString()
//        recyclerView = findViewById(R.id.rv_list)
//
//        imageView.setOnClickListener {
//            Log.d("클릭 시 dest", "$destinationUid")
//            val chatModel = ChatModel()
//            chatModel.users.put(uid.toString(), true)
//            chatModel.users.put(destinationUid!!, true)
//
//            val comment = ChatModel.Comment(uid, editText.text.toString(), curTime)
//            if(chatRoomUid == null){
//                imageView.isEnabled = false
//                fireDatabase.child("chatrooms").push().setValue(chatModel).addOnSuccessListener {
//                    //채팅방 생성
//                    checkChatRoom()
//                    //메세지 보내기
//                    Handler().postDelayed({
//                        println(chatRoomUid)
//                        fireDatabase.child("chatrooms").child(chatRoomUid.toString()).child("comments").push().setValue(comment)
//                        et_chatting.text = null
//                    }, 1000L)
//                    Log.d("chatUidNull dest", "$destinationUid")
//                }
//            }else{
//                fireDatabase.child("chatrooms").child(chatRoomUid.toString()).child("comments").push().setValue(comment)
//                et_chatting.text = null
//                Log.d("chatUidNotNull dest", "$destinationUid")
//            }
//        }
//        checkChatRoom()
//    }
//
//    private fun checkChatRoom(){
//        fireDatabase.child("chatrooms").orderByChild("users/$uid").equalTo(true)
//            .addListenerForSingleValueEvent(object : ValueEventListener{
//                override fun onCancelled(error: DatabaseError) {
//                }
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (item in snapshot.children){
//                        println(item)
//                        val chatModel = item.getValue<ChatModel>()
//                        if(chatModel?.users!!.containsKey(destinationUid)){
//                            chatRoomUid = item.key
//                            btn_send.isEnabled = true
//                            recyclerView?.layoutManager = LinearLayoutManager(this@MessageActivity)
//                            recyclerView?.adapter = RecyclerViewAdapter()
//                        }
//                    }
//                }
//            })
//    }
//
//
//    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.MessageViewHolder>() {
//
//        private val comments = ArrayList<ChatModel.Comment>()
//        private var friend : User? = null
//        init{
//            fireDatabase.child("users").child(destinationUid.toString()).addListenerForSingleValueEvent(object : ValueEventListener{
//                override fun onCancelled(error: DatabaseError) {
//                }
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    friend = snapshot.getValue<User>()
//                    //채팅 상단 이름
////                    messageActivity_textView_topName.text = friend?.name
//                    getMessageList()
//                }
//            })
//        }
//
//        fun getMessageList(){
//            fireDatabase.child("chatrooms").child(chatRoomUid.toString()).child("comments").addValueEventListener(object : ValueEventListener{
//                override fun onCancelled(error: DatabaseError) {
//                }
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    comments.clear()
//                    for(data in snapshot.children){
//                        val item = data.getValue<ChatModel.Comment>()
//                        comments.add(item!!)
//                        println(comments)
//                    }
//                    notifyDataSetChanged()
//                    //메세지를 보낼 시 화면을 맨 밑으로 내림
//                    recyclerView?.scrollToPosition(comments.size - 1)
//                }
//            })
//        }
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
//            val view : View = LayoutInflater.from(parent.context).inflate(R.layout.my_msgbox, parent, false)
//
//            return MessageViewHolder(view)
//        }
//        @SuppressLint("RtlHardcoded")
//        override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
//            holder.textView_message.textSize = 20F
//            holder.textView_message.text = comments[position].message
//            holder.textView_time.text = comments[position].time
//            if(comments[position].uid.equals(uid)){ // 본인 채팅
//                holder.textView_message.setBackgroundResource(R.drawable.mymsgbox)
//                holder.textView_name.visibility = View.INVISIBLE
//                holder.layout_destination.visibility = View.INVISIBLE
//                holder.layout_main.gravity = Gravity.RIGHT
//            }else{ // 상대방 채팅
//                Glide.with(holder.itemView.context)
//                    .load(friend?.profileImgUrl)
//                    .apply(RequestOptions().circleCrop())
//                    .into(holder.imageView_profile)
//                holder.textView_name.text = friend?.name
//                holder.layout_destination.visibility = View.VISIBLE
//                holder.textView_name.visibility = View.VISIBLE
//                holder.textView_message.setBackgroundResource(R.drawable.othermsgbox)
//                holder.layout_main.gravity = Gravity.LEFT
//            }
//        }
//
//        inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//            val textView_message: TextView = view.findViewById(R.id.messageItem_textView_message)
//            val textView_name: TextView = view.findViewById(R.id.messageItem_textview_name)
//            val imageView_profile: ImageView = view.findViewById(R.id.messageItem_imageview_profile)
//            val layout_destination: LinearLayout = view.findViewById(R.id.messageItem_layout_destination)
//            val layout_main: LinearLayout = view.findViewById(R.id.messageItem_linearlayout_main)
//            val textView_time : TextView = view.findViewById(R.id.messageItem_textView_time)
//        }
//
//        override fun getItemCount(): Int {
//            return comments.size
//        }
//
//    }
//}