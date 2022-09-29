package com.example.a22_2_sejong_project.utils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a22_2_sejong_project.R

class BoardDetailActivity : AppCompatActivity() {
    var contentUid: String? = null
    var destinationUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        contentUid = intent.getStringExtra("contentUid")
        destinationUid = intent.getStringExtra("destinationUid")
    }
}