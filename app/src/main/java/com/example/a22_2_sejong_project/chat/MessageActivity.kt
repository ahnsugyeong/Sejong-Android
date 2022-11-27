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
            }
        })
    }
}