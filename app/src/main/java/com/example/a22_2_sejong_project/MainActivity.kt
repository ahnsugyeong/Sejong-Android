package com.example.a22_2_sejong_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.a22_2_sejong_project.board.BoardFragment
import com.example.a22_2_sejong_project.chat.ChatFragment
import com.example.a22_2_sejong_project.databinding.ActivityMainBinding
import com.example.a22_2_sejong_project.home.HomeFragment
import com.example.a22_2_sejong_project.mypage.MyPageFragment
import com.example.a22_2_sejong_project.program.ProgramFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener {
    private var _Binding: ActivityMainBinding? = null
    private val binding get() = _Binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _Binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 바텀내비게이션 세팅
        binding.mainBottomNav.setOnNavigationItemSelectedListener(this)
        binding.mainBottomNav.selectedItemId = R.id.nav_item1
    }
    override fun onDestroy() {
        _Binding = null
        super.onDestroy()
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_item1 -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_container_layout,HomeFragment()).commit()
                binding.mainTitleTv.text = "홈"
                return true
            }
            R.id.nav_item2 -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_container_layout,BoardFragment()).commit()
                binding.mainTitleTv.text = "게시판"
                return true
            }
            R.id.nav_item3 -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_container_layout,ChatFragment()).commit()
                binding.mainTitleTv.text = "채팅"
                return true
            }
            R.id.nav_item4 -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_container_layout,ProgramFragment()).commit()
                binding.mainTitleTv.text = "교내 프로그램"
                return true
            }
            R.id.nav_item5 -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_container_layout,MyPageFragment()).commit()
                binding.mainTitleTv.text = "내정보"
                return true
            }
        }
        return false
    }
}
