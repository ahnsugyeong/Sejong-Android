package com.example.a22_2_sejong_project.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a22_2_sejong_project.databinding.FragmentHomeBinding
import org.jsoup.Jsoup

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        Thread(Runnable {
            val url = "https://do.sejong.ac.kr/ko/program/major"
            val doc = Jsoup.connect(url).get()
            val programList = doc.select("li div.content")
            val posterUrl = doc.select("li div.cover")
            val href = doc.select("ul.columns-4 a")


            this@HomeFragment.activity?.runOnUiThread {
                binding.homeDodreamRv.apply {
                    adapter = DodreamRvAdapter(requireContext(), programList, posterUrl, href)
                    layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                }
            }
        }).start()



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeCard1.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://library.sejong.ac.kr/studyroom/Main.ax"))
            startActivity(intent)
        }
        binding.homeCard2.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://do.sejong.ac.kr"))
            startActivity(intent)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}