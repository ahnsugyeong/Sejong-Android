package com.example.a22_2_sejong_project.utils

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.a22_2_sejong_project.DTO.BoardContentDTO
import com.example.a22_2_sejong_project.DTO.UserDTO
import com.example.a22_2_sejong_project.R
import com.example.a22_2_sejong_project.databinding.FragmentBoardDetailBinding
import com.example.a22_2_sejong_project.databinding.FragmentBoardMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_add_board_article.view.*


import kotlinx.android.synthetic.main.fragment_board_detail.*
import kotlinx.android.synthetic.main.fragment_board_detail.view.*
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
        destinationUid = arguments?.getString("destinationUid")
        destinationContentUid = arguments?.getString("destinationContentUid")
        boardCategory = arguments?.getString("boardCategory")

        firestore?.collection("user")?.document(destinationUid!!)?.get()?.addOnCompleteListener {
            if(it.isSuccessful) {
                var userDTO = it.result.toObject(UserDTO::class.java)
                binding.root.board_detail_userName.text = userDTO?.nickname
                binding.root.board_detail_major.text = userDTO?.major
                // profile image
                //Glide.with(this).load(userDTO?.프로필이미지링크).into(binding.root.board_detail_profileImage)
            }
        }

        firestore?.collection(boardCategory!!)?.document(destinationContentUid!!)?.get()?.addOnCompleteListener {
            if(it.isSuccessful) {
                var boardContentDTO = it.result.toObject(BoardContentDTO::class.java)
                binding.root.board_detail_title.text = boardContentDTO?.title
                binding.root.board_detail_description.text = boardContentDTO?.description
                binding.root.board_detail_timestamp.text = boardContentDTO?.timestamp
                binding.root.board_detail_headCount.text = boardContentDTO?.currentHeadCount.toString() + "/" + boardContentDTO?.totalHeadCount.toString()
                println("hey hey" + boardContentDTO?.contentType)
                when(boardContentDTO?.contentType) {
                    1 -> binding.root.board_detail_contentType.text = "모집"
                    2 -> binding.root.board_detail_contentType.text = "참여"
                    else -> binding.root.board_detail_contentType.text = "기타"
                }

            }
        }

        binding.root.board_detail_group.setOnClickListener {
            var groupMembers: ArrayList<String>? = null
            firestore?.collection(boardCategory!!)?.document(destinationContentUid!!)?.get()?.addOnCompleteListener {
                if(it.isSuccessful) {
                    var boardContentDTO = it.result.toObject(BoardContentDTO::class.java)
                    groupMembers = boardContentDTO?.groupMembers
                }
            }


            val menuList = arrayOf("오종석", "김해린", "안수경")
            val dialog = AlertDialog.Builder(
                requireActivity(),
                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar
            )
            dialog.setTitle("팀원 목록")
                .setItems(menuList, DialogInterface.OnClickListener { dialogInterface, i ->
                    // 클릭한 item의 마이페이지로 이동
                    
                })
            dialog.show()
        }
        binding.boardDetailBackBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}