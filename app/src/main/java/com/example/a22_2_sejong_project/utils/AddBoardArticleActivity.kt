package com.example.a22_2_sejong_project.utils

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.a22_2_sejong_project.DTO.BoardContentDTO
import com.example.a22_2_sejong_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_board_article.*
import java.text.SimpleDateFormat
import java.util.*

class AddBoardArticleActivity : AppCompatActivity() {
    private var storage: FirebaseStorage? = null
    private var firestore: FirebaseFirestore? = null
    var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_board_article)

        // Initiate
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        add_article_btn_upload.setOnClickListener {
            contentUpload()
        }
    }

    fun contentUpload() {
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var boardContentDTO = BoardContentDTO()

        //var storageRef = storage?.reference?.child("boardContents")?.child(fileName)

        boardContentDTO.title = add_article_title.text.toString()
        boardContentDTO.description = add_article_description.text.toString()
        boardContentDTO.timestamp = timestamp

        firestore?.collection("boardContents")?.document()?.set(boardContentDTO)

        setResult(Activity.RESULT_OK)
        finish()

    }
}