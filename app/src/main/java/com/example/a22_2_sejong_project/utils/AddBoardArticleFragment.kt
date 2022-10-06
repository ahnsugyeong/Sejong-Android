package com.example.a22_2_sejong_project.utils

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a22_2_sejong_project.DTO.BoardContentDTO
import com.example.a22_2_sejong_project.DTO.UserDTO
import com.example.a22_2_sejong_project.MainActivity
import com.example.a22_2_sejong_project.R
import com.example.a22_2_sejong_project.board.BoardFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_add_board_article.*

import kotlinx.android.synthetic.main.fragment_add_board_article.view.*
import java.text.SimpleDateFormat
import java.util.*


class AddBoardArticleFragment : Fragment() {
    private var storage: FirebaseStorage? = null
    private var firestore: FirebaseFirestore? = null
    var auth: FirebaseAuth? = null
    var type: Int? = 0
    var rootView: View? = null
    var boardCategory: String? = null
    var uid: String? = null
    var boardContentDTO: BoardContentDTO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val rootView = inflater.inflate(R.layout.fragment_add_board_article, container, false)

        // Initiate
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        uid = auth?.currentUser?.uid

        rootView.add_article_btn_upload.setOnClickListener {
            contentUpload()
        }

        rootView.radio_button_group.setOnCheckedChangeListener{ group, checkedId ->
            when(checkedId) {
                R.id.radio_recruit -> {
                    type = 1
                    headcount_textView.text = "모집 인원: "
                }
                R.id.radio_participate -> {
                    type = 2
                    headcount_textView.text = "참여 인원: "
                }
                R.id.radio_etc -> {
                    type = 3
                    headcount_textView.text = "기타 인원: "
                }
            }
        }

        boardCategory = arguments?.getString("boardCategory")

        return rootView
    }

    fun contentUpload() {

        firestore?.collection("user")?.document(uid!!)?.get()?.addOnCompleteListener {
            if(it.isSuccessful) {
                var timestamp = SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Date())
                boardContentDTO = BoardContentDTO()
                boardContentDTO?.title = add_article_title.text.toString()
                boardContentDTO?.description = add_article_description.text.toString()
                boardContentDTO?.timestamp = timestamp
                boardContentDTO?.contentType = type!!
                boardContentDTO?.userId = auth?.currentUser?.email


                val userDTO = it.result.toObject(UserDTO::class.java)
                boardContentDTO?.uId = userDTO?.uid
                boardContentDTO?.nickname = userDTO?.nickname

                if(headcount_editText.toString() == "") boardContentDTO?.totalHeadCount = -1
                else boardContentDTO?.totalHeadCount = headcount_editText.text.toString().toInt()

                firestore?.collection(boardCategory!!)?.document()?.set(boardContentDTO!!)

                var bundle = Bundle()
                bundle.putString("boardCategoryReturn", boardCategory)
                val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
                val BoardArticleFragment = BoardFragment()
                BoardArticleFragment.arguments = bundle
                transaction.replace(R.id.main_container_layout, BoardArticleFragment)
                transaction.commit()
            }
        }


    }
    override fun onDestroyView() {
        super.onDestroyView()
        rootView = null
    }
}