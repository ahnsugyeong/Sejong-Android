package com.example.a22_2_sejong_project.chat

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a22_2_sejong_project.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.fragment_chat.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentUser: String //현재닉네임->fireauth
    private val db = FirebaseDatabase.getInstance()
    private lateinit var registration: ListenerRegistration //문서수신
    private val chatList = arrayListOf<ChatLayout>() //리사이클러 뷰 목록
    private lateinit var adapter: ChatAdapter //리사이클러 뷰 어댑터
    private val database = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentUser = FirebaseAuth.getInstance().currentUser?.uid.toString()

//        db.reference.child("data").child(currentUser)
//        val userInfoMap = mutableMapOf<String, Any>()
//        userInfoMap["userId"] = FirebaseAuth.getInstance().currentUser?.uid.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val view = binding.root


        //리사이클러뷰 설정
        binding.rvList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = ChatAdapter(currentUser, chatList)
        binding.rvList.adapter = adapter

        //채팅창이 공백일 경우 버튼 비활성화
        binding.etChatting.addTextChangedListener { text ->
            binding.btnSend.isEnabled = text.toString() != ""
        }
        var name = "12"
        FirebaseFirestore.getInstance().collection("user")
            .whereEqualTo("uid",currentUser)
            .get()
            .addOnSuccessListener { documnets->
                for(doc in documnets){
                    doc.get("nickname")?.let {
                        name = it.toString()
                        println("닉네임은 $name")
                    }
                }
            }
//            .addSnapshotListener() { value, e->
//                if (e != null) {
//                    Log.w(TAG, "Listen failed.",e)
//                    return@addSnapshotListener
//                }
//                val user = ArrayList<String>()
//                for (doc in value!!){
//                    doc.getString("nickname")?.let{
//                        name = it
//                        println("닉네임은 $name")
//                    }
//                }
//            }
        //Toast.makeText(context, "현재 닉네임은 ${name}입니다.", Toast.LENGTH_SHORT).show()
        //입력버튼

        binding.btnSend.setOnClickListener {
            //입력 데이터
            val sf = SimpleDateFormat("a hh:mm",Locale.KOREA)
            sf.timeZone = TimeZone.getTimeZone("Asia/Seoul")
            val time = sf.format(System.currentTimeMillis())
            val data = hashMapOf(
                "nickname" to name,
                "contents" to binding.etChatting.text.toString(),
                "time" to time,
                "uid" to currentUser
            )

//           이대로 하자!!!! db.getReference("message").push().setValue("hello")
            db.getReference("Chat").child("message").push().setValue(data)
                .addOnSuccessListener  {
                    binding.etChatting.text.clear()
                    Log.w("ChatFragment","Document added: $it")
                }
                .addOnFailureListener{ e->
                    Toast.makeText(context, "전송하는데 실패했습니다.",Toast.LENGTH_SHORT).show()
                    Log.w("ChatFragment","Error occurs: $e")
                }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //chatList.add(ChatLayout("알림", "$currentUser 닉네임으로 입장했습니다.",""))

        db.getReference("Chat").child("message").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapshot in snapshot.children) {
                    val chatItem =dataSnapshot.getValue<ChatModel.Comment>()
                    val nickname = chatItem?.nickname
                    val contents = chatItem?.contents
                    val uid = chatItem?.uid
                    val sf = SimpleDateFormat("a hh:mm",Locale.KOREA)
                    sf.timeZone = TimeZone.getTimeZone("Asia/Seoul")
                    val time = sf.format(System.currentTimeMillis())
                    val item : ChatLayout
                    if (uid != currentUser) item = ChatLayout(nickname,contents,time,1, uid)
                    else item = ChatLayout(nickname, contents, time, 0, uid)
                    chatList.add(item)
                }
                adapter.notifyDataSetChanged()
                rv_list.scrollToPosition(chatList.size - 1)

            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "전송하는데 실패했습니다!!.",Toast.LENGTH_SHORT).show()
                Log.w(TAG,"Failed to read value.",error.toException())
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



//    companion object{
//        fun newInstance() : ChatFragment {
//            return ChatFragment()
//        }
//    }
//    private val fireDatabase = FirebaseDatabase.getInstance().reference
//    private var myuid: String? = null
//    private var destUid: String? = null
//    //메모리에 올라갔을 때
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }
//
//    //프레그먼트를 포함하고 있는 액티비티에 붙었을 때
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//    }
//
//    //뷰가 생성되었을 때
//    //프레그먼트와 레이아웃을 연결시켜주는 부분
//    override fun onCreateView(inflater: LayoutInflater,
//                              container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        val view = inflater.inflate(R.layout.fragment_chat, container, false)
//        val recyclerView = view.findViewById<RecyclerView>(R.id.chatfragment_recyclerview)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.adapter = RecyclerViewAdapter()
//
//
//        return view
//    }
//
//    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder>() {
//
//        private val chatModel = ArrayList<ChatModel>()
//        private var uid : String? = null
//        private val destinationUsers : ArrayList<String> = arrayListOf()
//
//
//
//        init {
//            uid = Firebase.auth.currentUser?.uid.toString()
//            println(uid)
//
//            fireDatabase.child("chatrooms").orderByChild("users/$uid").equalTo(true).addListenerForSingleValueEvent(object : ValueEventListener{
//                override fun onCancelled(error: DatabaseError) {
//                }
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    chatModel.clear()
//                    for(data in snapshot.children){
//                        chatModel.add(data.getValue<ChatModel>()!!)
//                        println(data)
//                    }
//                    notifyDataSetChanged()
//                }
//
//            })
//
//        }
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
//
//
//            return CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_list, parent, false))
//        }
//
//        inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//            val imageView: ImageView = itemView.findViewById(R.id.chat_item_imageview)
//            val textView_title : TextView = itemView.findViewById(R.id.chat_textview_title)
//            val textView_lastMessage : TextView = itemView.findViewById(R.id.chat_item_textview_lastmessage)
//        }
//
//        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
//            var destinationUid: String? = null
//            //채팅방에 있는 유저 모두 체크
//            for (user in chatModel[position].users.keys) {
//                if (!user.equals(uid)) {
//                    destinationUid = user
//                    destinationUsers.add(destinationUid)
//                }
//            }
//            fireDatabase.child("users").child("$destinationUid").addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onCancelled(error: DatabaseError) {
//                }
//
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val friend = snapshot.getValue<Friend>()
//                    Glide.with(holder.itemView.context).load(friend?.profileImageUrl)
//                        .apply(RequestOptions().circleCrop())
//                        .into(holder.imageView)
//                    holder.textView_title.text = friend?.name
//                }
//            })
//            //메세지 내림차순 정렬 후 마지막 메세지의 키값을 가져
//            val commentMap = TreeMap<String, ChatModel.Comment>(reverseOrder())
//            commentMap.putAll(chatModel[position].comments)
//            val lastMessageKey = commentMap.keys.toTypedArray()[0]
//            holder.textView_lastMessage.text = chatModel[position].comments[lastMessageKey]?.message
//
//            //채팅창 선책 시 이동
////            holder.itemView.setOnClickListener {
////                val intent = Intent(context, MessageActivity::class.java)
////                intent.putExtra("destinationUid", destinationUsers[position])
////                context?.startActivity(intent)
////            }
//
//        }
//
//        override fun getItemCount(): Int {
//            return chatModel.size
//        }
//    }

