package com.example.a22_2_sejong_project.program

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.a22_2_sejong_project.R
import com.example.a22_2_sejong_project.databinding.FragmentMyPageBinding
import com.example.a22_2_sejong_project.databinding.FragmentProgramBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.jsoup.Jsoup

class ProgramFragment : Fragment() {
    private var _binding: FragmentProgramBinding? = null
    private val binding get() = _binding!!
    private val tabTitleArray = arrayOf(
        "학습역량강화",
        "전공역량강화"
    )
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProgramBinding.inflate(inflater, container, false)

        binding.programViewPager.adapter = ViewPagerAdapter(activity?.supportFragmentManager!!,lifecycle)
        TabLayoutMediator(binding.programTabLayout,binding.programViewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()

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