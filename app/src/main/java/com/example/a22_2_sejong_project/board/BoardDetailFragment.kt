package com.example.a22_2_sejong_project.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.a22_2_sejong_project.DTO.BoardContentDTO
import com.example.a22_2_sejong_project.DTO.UserDTO
import com.example.a22_2_sejong_project.R
import com.example.a22_2_sejong_project.databinding.FragmentBoardDetailBinding
import com.example.a22_2_sejong_project.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.board_delete_dialog.view.*

import kotlinx.android.synthetic.main.fragment_board_detail.*
import kotlinx.android.synthetic.main.fragment_board_detail.view.*
import kotlinx.android.synthetic.main.group_dialog.*
import kotlinx.android.synthetic.main.group_dialog.view.*
import kotlinx.android.synthetic.main.item_group_dialog.view.*
import java.util.*
import kotlin.collections.ArrayList

class BoardDetailFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    var contentUid: String? = null
    var destinationUid: String? = null
    var destinationContentUid: String? = null
    var firestore: FirebaseFirestore? = null
    var auth: FirebaseAuth? = null
    var uid: String? = null
    var boardCategory: String? = null
    private var groupMemberUIds: ArrayList<String> = arrayListOf()
    private var groupMemberPositions: ArrayList<String> = arrayListOf()


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
        //contentUid = arguments?.getString("contentUid")
        destinationUid = arguments?.getString("destinationUid")
        destinationContentUid = arguments?.getString("destinationContentUid")
        boardCategory = arguments?.getString("boardCategory")



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
                    if (boardContentDTO?.favorites?.containsKey(uid) == true) binding.root.board_detail_heartImg.setImageResource(R.drawable.heart_on)
                    else binding.root.board_detail_heartImg.setImageResource(R.drawable.heart_off)
                    binding.root.board_detail_heartNum.text = boardContentDTO?.favoriteCount.toString()
                    binding.root.board_detail_commentNum.text = boardContentDTO?.commentCount.toString()

                    when (boardContentDTO?.contentType) {
                        1 -> binding.root.board_detail_contentType.text = "모집"
                        2 -> binding.root.board_detail_contentType.text = "참여"
                        else -> binding.root.board_detail_contentType.text = "기타"
                    }
                }
            }


        // show group member
        binding.root.board_detail_group.setOnClickListener {
            groupMemberUIds!!.clear()
            groupMemberPositions!!.clear()

            firestore?.collection(boardCategory!!)?.document(destinationContentUid!!)?.get()
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        var boardContentDTO = it.result.toObject(BoardContentDTO::class.java)
                        groupMemberUIds = boardContentDTO?.groupMemberUIds!!
                        groupMemberPositions = boardContentDTO?.groupMemberPositions!!

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
                }
        }

        binding.root.board_detail_heart.setOnClickListener {
            favoriteEvent()
        }

        binding.boardDetailBackBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.main_container_layout,
                BoardFragment()
            ).commit()
        }

        if (destinationUid!! != uid!!) {
            binding.root.board_detail_menu_btn.visibility = View.INVISIBLE
        } else {
            binding.root.board_detail_menu_btn.visibility = View.VISIBLE
            binding.root.board_detail_menu_btn.setOnClickListener {
                showPopup(binding.root.board_detail_menu_btn)
            }
        }


        // show comment fragment

        val boardCommentFragment = BoardCommentFragment()
        val bundle = Bundle()
        bundle.putString("contentUid", destinationContentUid)
        bundle.putString("boardCategory", boardCategory)
        boardCommentFragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.board_detail_container_layout,
            boardCommentFragment
        ).commit()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun favoriteEvent() {
        var tsDoc = firestore?.collection(boardCategory!!)?.document(destinationContentUid!!)
        firestore?.runTransaction { transaction ->

            var boardContentDTO = transaction.get(tsDoc!!).toObject(BoardContentDTO::class.java)

            if(boardContentDTO!!.favorites.containsKey(uid)) {
                // when the button is clicked
                boardContentDTO?.favoriteCount = boardContentDTO?.favoriteCount - 1
                binding.root.board_detail_heartNum.text = boardContentDTO?.favoriteCount.toString()
                boardContentDTO?.favorites.remove(uid)
                binding.root.board_detail_heartImg.setImageResource(R.drawable.heart_off)
            } else {
                // when the button is not clicked
                boardContentDTO?.favoriteCount = boardContentDTO?.favoriteCount + 1
                binding.root.board_detail_heartNum.text = boardContentDTO?.favoriteCount.toString()
                boardContentDTO?.favorites[uid!!] = true
                binding.root.board_detail_heartImg.setImageResource(R.drawable.heart_on)
            }
            transaction.set(tsDoc, boardContentDTO)
        }

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
            val memberNickname: String? = null
            for (member in groupMemberUIds) {
                firestore?.collection("user")?.document(member)?.get()
                    ?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            val userDTO = it.result.toObject(UserDTO::class.java)
                            // profile image
                            Glide.with(requireContext()).load(userDTO?.profileUrl)
                                .apply(RequestOptions().circleCrop())
                                .into(viewholder.item_group_dialog_profileImage)

                            // userId
                            viewholder.item_group_dialog_nickname.text = userDTO?.nickname
                        }
                    }
            }

            // position
            viewholder.item_group_dialog_position.text = groupMemberPositions!![position]


            // 게시물 클릭시
            viewholder.item_group_dialog_object.setOnClickListener { v ->
                // 해당 유저의 마이페이지로 이동
            }
        }

        override fun getItemCount(): Int {
            return groupMemberUIds!!.size
        }
    }
    private fun showPopup(v: View) {
        val popup = PopupMenu(context, v)
        popup.menuInflater.inflate(R.menu.menu_option, popup.menu)
        popup.setOnMenuItemClickListener(this)
        popup.show()
    }

    override fun onMenuItemClick(p0: MenuItem?): Boolean {
        when(p0?.itemId) {
            R.id.menu_update -> {
                // update 화면 띄우기
                val updateBoardArticleFragment = UpdateBoardArticleFragment()
                val update_bundle = Bundle()

                update_bundle.putString("destinationUid", destinationUid)
                update_bundle.putString("boardCategory", boardCategory)
                update_bundle.putString("destinationContentUid", destinationContentUid)
                updateBoardArticleFragment.arguments = update_bundle

                requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
                requireActivity().supportFragmentManager.beginTransaction().replace(
                    R.id.main_container_layout,
                    updateBoardArticleFragment
                ).commit()
            }
            R.id.menu_delete -> {
                // dialog 띄우기
                val dialogBuilder = AlertDialog.Builder(
                    requireActivity()
                )
                val alertDialog = dialogBuilder.create()
                val dialogView = layoutInflater.inflate(R.layout.board_delete_dialog, null)
                alertDialog.setView(dialogView)
                alertDialog.show()

                dialogView.board_delete_dialog_ok_button.setOnClickListener {
                    // 해당 게시물 삭제
                    deleteArticle()
                    alertDialog.dismiss()
                }
                dialogView.board_delete_dialog_cancel_button.setOnClickListener {
                    alertDialog.dismiss()
                }
            }
        }
        return p0 != null
    }

    fun deleteArticle() {
        val bucket = firestore?.collection(boardCategory!!)
        bucket!!.document(destinationContentUid!!).delete()
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        requireActivity().supportFragmentManager.beginTransaction().replace(
            R.id.main_container_layout,
            BoardFragment()
        ).commit()

    }

}

