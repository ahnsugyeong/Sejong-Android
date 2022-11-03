package com.example.a22_2_sejong_project.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.a22_2_sejong_project.DTO.BoardContentDTO
import com.example.a22_2_sejong_project.DTO.UserDTO
import com.example.a22_2_sejong_project.R
import com.example.a22_2_sejong_project.databinding.FragmentBoardCommentBinding
import com.example.a22_2_sejong_project.databinding.FragmentBoardDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_board_comment.*
import kotlinx.android.synthetic.main.fragment_board_comment.view.*
import kotlinx.android.synthetic.main.fragment_board_detail.view.*
import kotlinx.android.synthetic.main.item_board_comment.view.*
import kotlinx.android.synthetic.main.item_group_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class BoardCommentFragment : Fragment() {
    var contentUid: String? = null
    var boardCategory: String? = null

    private var _binding: FragmentBoardCommentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBoardCommentBinding.inflate(inflater, container, false)

        binding.root.board_comment_recyclerView.adapter = BoardCommentRecyclerView()
        binding.root.board_comment_recyclerView.layoutManager = LinearLayoutManager(activity)

        binding.root.board_comment_send_button.setOnClickListener {
            var comment = BoardContentDTO.Comment()
            comment.userId = FirebaseAuth.getInstance().currentUser?.email
            comment.uid = FirebaseAuth.getInstance().currentUser?.uid
            comment.comment = add_board_comment_editText.text.toString()
            comment.timestamp = SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Date())

            contentUid = arguments?.getString("contentUid")
            boardCategory = arguments?.getString("boardCategory")

            FirebaseFirestore.getInstance().collection(boardCategory!!)
                .document(contentUid!!)
                .collection("comments").document().set(comment)

            add_board_comment_editText.setText("")

            var tsDoc = FirebaseFirestore.getInstance()?.collection(boardCategory!!)?.document(contentUid!!)
            FirebaseFirestore.getInstance()?.runTransaction { transaction ->
                var boardContentDTO = transaction.get(tsDoc!!).toObject(BoardContentDTO::class.java)
                boardContentDTO?.commentCount = boardContentDTO?.commentCount?.plus(1)!!
                transaction.set(tsDoc, boardContentDTO)
            }

        }
        return binding.root
    }


    inner class BoardCommentRecyclerView : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var comments: ArrayList<BoardContentDTO.Comment> = arrayListOf()

        init {
            contentUid = arguments?.getString("contentUid")
            boardCategory = arguments?.getString("boardCategory")
            FirebaseFirestore.getInstance()
                .collection(boardCategory!!)
                .document(contentUid!!)
                .collection("comments")
                .orderBy("timestamp")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    comments.clear()
                    if(querySnapshot == null)return@addSnapshotListener

                    for(snapshot in querySnapshot.documents!!) {
                        comments.add(snapshot.toObject(BoardContentDTO.Comment::class.java)!!)
                    }
                    notifyDataSetChanged()
                }

        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context)
                .inflate(com.example.a22_2_sejong_project.R.layout.item_board_comment, parent, false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            var viewholder = (holder as CustomViewHolder).itemView

            viewholder.board_comment_textView_timestamp.text = comments[position].timestamp
            viewholder.board_comment_textView_comment.text = comments[position].comment

            FirebaseFirestore.getInstance().collection("user")?.document(comments[position].uid!!).get()
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        val userDTO = it.result.toObject(UserDTO::class.java)
                        // profile image
                        Glide.with(requireContext()).load(userDTO?.profileUrl)
                            .apply(RequestOptions().circleCrop())
                            .into(viewholder.board_comment_imageView_profile)

                        // userId
                        viewholder.board_comment_textView_profile.text = userDTO?.nickname
                    }
                }

        }

        override fun getItemCount(): Int {
            return comments.size
        }
    }


}