package com.example.a22_2_sejong_project.utils

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a22_2_sejong_project.DTO.BoardContentDTO
import com.example.a22_2_sejong_project.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_add_board_article.*
import java.text.SimpleDateFormat
import java.util.*

class BoardDetailFragment : Fragment() {
    var contentUid: String? = null
    var destinationUid: String? = null

    var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_board_detail, container, false)



        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rootView = null
    }

}