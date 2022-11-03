package com.example.a22_2_sejong_project.program

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a22_2_sejong_project.MainActivity
import com.example.a22_2_sejong_project.R
import com.example.a22_2_sejong_project.databinding.FragmentLearnBinding
import com.example.a22_2_sejong_project.databinding.FragmentProgramBinding
import com.example.a22_2_sejong_project.home.DodreamRvAdapter
import org.jsoup.Jsoup

class Learn : Fragment() {
    private var _binding: FragmentLearnBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLearnBinding.inflate(inflater, container, false)


        Thread(Runnable {
            val url = "https://do.sejong.ac.kr/ko/program/learn"
            val doc = Jsoup.connect(url).get()
            val programList = doc.select("ul.columns-4")

            this@Learn.activity?.runOnUiThread {
                binding.programLearnRv.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = LearnAdapter(requireContext(),programList)
                }
                binding.learnPb.visibility = View.GONE
            }
        }).start()

        return binding.root
    }
}
