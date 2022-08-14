package com.example.a22_2_sejong_project.mypage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.a22_2_sejong_project.R
import com.example.a22_2_sejong_project.databinding.FragmentMyPageBinding
import com.example.a22_2_sejong_project.login.SejongLoginActivity
import com.google.firebase.auth.FirebaseAuth

class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)

        // 로그아웃
        binding.logout.setOnClickListener {
            Toast.makeText(context,"로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(context,SejongLoginActivity::class.java))
        }
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}