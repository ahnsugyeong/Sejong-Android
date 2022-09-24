package com.example.a22_2_sejong_project.board

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a22_2_sejong_project.DTO.BoardContentDTO
import com.example.a22_2_sejong_project.R
import com.example.a22_2_sejong_project.databinding.FragmentBoardMainBinding
import com.example.a22_2_sejong_project.utils.BoardCommentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_board_main.view.*
import kotlinx.android.synthetic.main.item_board_main.view.*

class BoardFragment : Fragment() {
    var firestore: FirebaseFirestore? = null
    var uid: String? = null

    private var _binding: FragmentBoardMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBoardMainBinding.inflate(inflater, container, false)

        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid

        binding.root.board_main_recyclerView.adapter = BoardRecyclerViewAdapter()
        binding.root.board_main_recyclerView.layoutManager = LinearLayoutManager(activity)

        return binding.root
    }

    inner class BoardRecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var boardContentDTOs: ArrayList<BoardContentDTO> = arrayListOf()
        var contentUIdList: ArrayList<String> = arrayListOf()



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_board_main, parent, false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View): RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewholder = (holder as CustomViewHolder).itemView

            // title
            viewholder.item_board_main_title.text = boardContentDTOs!![position].title

            // description
            viewholder.item_board_main_description.text = boardContentDTOs!![position].description

            // timestamp
            viewholder.item_board_main_timestamp.text = System.currentTimeMillis().toString()

            // 게시물 클릭시
            viewholder.item_board_main_object.setOnClickListener { v ->
                var intent = Intent(v.context, BoardCommentActivity::class.java)
                intent.putExtra("contentUid", contentUIdList[position])
                intent.putExtra("destinationUid", boardContentDTOs[position].uid)
                startActivity(intent)
            }

        }

        override fun getItemCount(): Int {
            return boardContentDTOs.size
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}