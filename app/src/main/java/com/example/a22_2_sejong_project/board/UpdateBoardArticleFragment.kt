package com.example.a22_2_sejong_project.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a22_2_sejong_project.DTO.BoardContentDTO
import com.example.a22_2_sejong_project.DTO.UserDTO
import com.example.a22_2_sejong_project.MainActivity
import com.example.a22_2_sejong_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_add_board_article.*
import kotlinx.android.synthetic.main.fragment_add_board_article.view.*
import kotlinx.android.synthetic.main.fragment_board_detail.view.*
import kotlinx.android.synthetic.main.fragment_update_board_article.*
import kotlinx.android.synthetic.main.fragment_update_board_article.view.*
import java.text.SimpleDateFormat
import java.util.*


class UpdateBoardArticleFragment : Fragment() {
    private var storage: FirebaseStorage? = null
    private var firestore: FirebaseFirestore? = null
    var auth: FirebaseAuth? = null
    var type: Int? = 1
    var boardCategory: String? = null
    var uid: String? = null
    var boardContentDTO: BoardContentDTO? = null
    var contentUid: String? = null
    var rootView: View? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_update_board_article, container, false)

        // Initiate
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        uid = auth?.currentUser?.uid
        contentUid = arguments?.getString("contentUid")
        boardCategory = arguments?.getString("boardCategory")
        println("content Uid = " + contentUid)
        println("boardCategory = " + boardCategory)
        firestore?.collection(boardCategory!!)?.document(contentUid!!)?.get()?.addOnCompleteListener {
            if (it.isSuccessful) {
                boardContentDTO = it.result.toObject(BoardContentDTO::class.java)
                when(boardContentDTO?.contentType) {
                    1 -> {
                        rootView?.update_radio_button_group?.check(R.id.update_radio_recruit)
                        rootView?.board_update_headcount_textView?.text = "모집 인원: "
                    }
                    2 -> {
                        rootView?.update_radio_button_group?.check(R.id.update_radio_participate)
                        rootView?.board_update_headcount_textView?.text = "참여 인원: "
                    }
                    3 -> {
                        rootView?.update_radio_button_group?.check(R.id.update_radio_etc)
                        rootView?.board_update_headcount_textView?.text = "기타 인원: "
                    }
                }
                rootView?.board_update_headcount_editText?.setText(boardContentDTO?.totalHeadCount?.toString())
                rootView?.update_article_title?.setText(boardContentDTO?.title)
                rootView?.update_article_description?.setText(boardContentDTO?.description)
            }
        }

        rootView?.update_article_btn_upload?.setOnClickListener {
            contentUpdate()
        }

        rootView?.update_board_article_back_btn?.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.main_container_layout,
                BoardFragment()
            ).commit()
        }

        rootView?.update_radio_button_group?.setOnCheckedChangeListener{ group, checkedId ->
            when(checkedId) {
                R.id.update_radio_recruit -> {
                    type = 1
                    board_update_headcount_textView.text = "모집 인원: "
                }
                R.id.update_radio_participate -> {
                    type = 2
                    board_update_headcount_textView.text = "참여 인원: "
                }
                R.id.update_radio_etc -> {
                    type = 3
                    board_update_headcount_textView.text = "기타 인원: "
                }
                else -> {
                    type = 1
                    board_update_headcount_textView.text = "모집 인원: "
                }
            }
        }

        return rootView
    }

    fun contentUpdate() {

        var tsDoc = firestore?.collection(boardCategory!!)?.document(contentUid!!)
        firestore?.runTransaction { transaction ->
            boardContentDTO = transaction.get(tsDoc!!).toObject(BoardContentDTO::class.java)
            boardContentDTO?.title = update_article_title.text.toString()
            boardContentDTO?.description = update_article_description.text.toString()
            boardContentDTO?.contentType = type!!
            if(board_update_headcount_editText.toString() == "") boardContentDTO?.totalHeadCount = -1
            else boardContentDTO?.totalHeadCount = board_update_headcount_editText.text.toString().toInt()

            firestore?.collection(boardCategory!!)?.document(boardContentDTO?.contentId!!)?.set(boardContentDTO!!)
            transaction.set(tsDoc, boardContentDTO!!)
            println("update success")

            var bundle = Bundle()
            bundle.putString("boardCategoryReturn", boardCategory)
            val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
            val BoardArticleFragment = BoardFragment()
            BoardArticleFragment.arguments = bundle
            transaction.replace(R.id.main_container_layout, BoardArticleFragment)
                .addToBackStack(null)
                .commit()
        }

//        firestore?.collection("user")?.document(uid!!)?.get()?.addOnCompleteListener {
//            if(it.isSuccessful) {
//                // timestamp 수정된 시간 or 생성된 시간?
//                // var timestamp = SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Date())
//                // boardContentDTO?.timestamp = timestamp
//
//                // 수정된 글 정보 DTO에 저장
//                boardContentDTO?.title = update_article_title.text.toString()
//                boardContentDTO?.description = update_article_description.text.toString()
//                boardContentDTO?.contentType = type!!
//
////                val userDTO = it.result.toObject(UserDTO::class.java)
////                boardContentDTO?.uId = userDTO?.uid
////                boardContentDTO?.nickname = userDTO?.nickname
////                boardContentDTO?.contentId = boardContentDTO?.uId + boardContentDTO?.timestamp
//
//                if(headcount_editText.toString() == "") boardContentDTO?.totalHeadCount = -1
//                else boardContentDTO?.totalHeadCount = headcount_editText.text.toString().toInt()
//
//                firestore?.collection(boardCategory!!)?.document(boardContentDTO?.contentId!!)?.set(boardContentDTO!!)
//
//                var bundle = Bundle()
//                bundle.putString("boardCategoryReturn", boardCategory)
//                val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
//                val BoardArticleFragment = BoardFragment()
//                BoardArticleFragment.arguments = bundle
//                transaction.replace(R.id.main_container_layout, BoardArticleFragment)
//                    .addToBackStack(null)
//                    .commit()
//            }
//        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        rootView = null
    }
}