package com.example.a22_2_sejong_project.program

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a22_2_sejong_project.R
import com.example.a22_2_sejong_project.databinding.FragmentMyPageBinding
import com.example.a22_2_sejong_project.databinding.FragmentProgramBinding
import org.jsoup.Jsoup

class ProgramFragment : Fragment() {
    private var _binding: FragmentProgramBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProgramBinding.inflate(inflater, container, false)

//        Thread(Runnable {
//            val url = "https://do.sejong.ac.kr/ko/program/career"
//            val doc = Jsoup.connect(url).get()
//            val data = doc.select()
//        })



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}