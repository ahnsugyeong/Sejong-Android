package com.example.a22_2_sejong_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.a22_2_sejong_project.databinding.ActivityMainBinding
import com.example.a22_2_sejong_project.mypage.MyPageFragment
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

            }
            R.id.nav_item2 -> {

            }
            R.id.nav_item3 -> {

            }
            R.id.nav_item4 -> {

            }
            R.id.nav_item5 -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_container_layout,MyPageFragment()).commit()
                return true
            }
        }
        return false
    }
}
