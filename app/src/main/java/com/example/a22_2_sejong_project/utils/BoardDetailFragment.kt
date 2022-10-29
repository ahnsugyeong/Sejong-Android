package com.example.a22_2_sejong_project.utils

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.a22_2_sejong_project.DTO.BoardContentDTO
import com.example.a22_2_sejong_project.DTO.UserDTO
import com.example.a22_2_sejong_project.R
import com.example.a22_2_sejong_project.board.BoardFragment
import com.example.a22_2_sejong_project.databinding.FragmentBoardDetailBinding
import com.example.a22_2_sejong_project.databinding.FragmentBoardMainBinding
import com.example.a22_2_sejong_project.databinding.GroupDialogBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

import kotlinx.android.synthetic.main.fragment_board_detail.*
import kotlinx.android.synthetic.main.fragment_board_detail.view.*
import kotlinx.android.synthetic.main.group_dialog.*
import kotlinx.android.synthetic.main.group_dialog.view.*
import kotlinx.android.synthetic.main.item_group_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BoardDetailFragment : Fragment() {
    var contentUid: String? = null
    var destinationUid: String? = null
    var destinationContentUid: String? = null
    var firestore: FirebaseFirestore? = null
    var auth: FirebaseAuth? = null
    var uid: String? = null
    var boardCategory: String? = null
    private var boardGroupUids: MutableList<String> = mutableListOf()
    private var boardGroupPositions: MutableList<String> = mutableListOf()

    private var _binding: FragmentBoardDetailBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBoardDetailBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid
        auth = FirebaseAuth.getInstance()
        contentUid = arguments?.getString("contentUid")
        destinationUid = arguments?.getString("destinationUid")
        destinationContentUid = arguments?.getString("destinationContentUid")
        boardCategory = arguments?.getString("boardCategory")
       // val boardGroupRecyclerViewAdapter = BoardGroupRecyclerViewAdapter()


        // board detail fragment
        firestore?.collection("user")?.document(destinationUid!!)?.get()?.addOnCompleteListener {
            if (it.isSuccessful) {
                var userDTO = it.result.toObject(UserDTO::class.java)
                binding.root.board_detail_userName.text = userDTO?.nickname
                binding.root.board_detail_major.text = userDTO?.major
                // profile image
                Glide.with(requireContext()).load(userDTO?.profileUrl)
                    .apply(RequestOptions().circleCrop())
                    .into(binding.root.board_detail_profileImage)
            }
        }

        firestore?.collection(boardCategory!!)?.document(destinationContentUid!!)?.get()
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    var boardContentDTO = it.result.toObject(BoardContentDTO::class.java)
                    binding.root.board_detail_title.text = boardContentDTO?.title
                    binding.root.board_detail_description.text = boardContentDTO?.description
                    binding.root.board_detail_timestamp.text = boardContentDTO?.timestamp
                    binding.root.board_detail_headCount.text =
                        boardContentDTO?.currentHeadCount.toString() + "/" + boardContentDTO?.totalHeadCount.toString()
                    when (boardContentDTO?.contentType) {
                        1 -> binding.root.board_detail_contentType.text = "모집"
                        2 -> binding.root.board_detail_contentType.text = "참여"
                        else -> binding.root.board_detail_contentType.text = "기타"
                    }
                }
            }


        // show group member
        binding.root.board_detail_group.setOnClickListener {
            boardGroupUids.clear()
            boardGroupPositions.clear()

            boardGroupUids.add("uid1")
            boardGroupUids.add("uid2")
            boardGroupPositions.add("position1")
            boardGroupPositions.add("position2")
//            firestore?.collection(boardCategory!!)?.document(destinationContentUid!!)?.get()
//                ?.addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        var boardContentDTO = it.result.toObject(BoardContentDTO::class.java)
//                        boardGroupUids = boardContentDTO?.groupMembers?.keys?.toMutableList()!!
//
//                        for (key in boardGroupUids) {
//                            boardGroupPositions.add(boardContentDTO?.groupMembers!![key]!!)
//                        }
//                    }
//                }

            val dialog = AlertDialog.Builder(
                requireActivity(),
                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar
            )
            val dialogView = layoutInflater.inflate(R.layout.group_dialog, null)

            dialog.setTitle("팀원 목록")
            dialog.setView(dialogView)
            dialogView.group_dialog_recyclerview.layoutManager = LinearLayoutManager(activity)
            dialogView.group_dialog_recyclerview.adapter = BoardGroupRecyclerViewAdapter()


            dialog.show()
        }

        binding.boardDetailBackBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.main_container_layout,
                BoardFragment()
            ).commit()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    inner class BoardGroupRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context)
                .inflate(com.example.a22_2_sejong_project.R.layout.item_group_dialog, parent, false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            var viewholder = (holder as CustomViewHolder).itemView

//            for (member in boardGroupUids) {
//                firestore?.collection("user")?.document(member)?.get()
//                    ?.addOnCompleteListener {
//                        if (it.isSuccessful) {
//                            val userDTO = it.result.toObject(UserDTO::class.java)
//                            // profile image
//                            Glide.with(requireContext()).load(userDTO?.profileUrl)
//                                .apply(RequestOptions().circleCrop())
//                                .into(viewholder.item_group_dialog_profileImage)
//                        }
//                    }
//            }

            // userId
            viewholder.item_group_dialog_nickname.text = boardGroupUids[position]

            // position
            viewholder.item_group_dialog_position.text = boardGroupPositions[position]


            // 게시물 클릭시
            viewholder.item_group_dialog_object.setOnClickListener { v ->
                // 해당 유저의 마이페이지로 이동
            }
        }

        override fun getItemCount(): Int {
            return boardGroupUids.size
        }
    }


}

