package com.example.a22_2_sejong_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.a22_2_sejong_project.board.BoardFragment
import com.example.a22_2_sejong_project.chat.ChatFragment
import com.example.a22_2_sejong_project.databinding.ActivityMainBinding
import com.example.a22_2_sejong_project.home.HomeFragment
import com.example.a22_2_sejong_project.mypage.MyPageFragment
import com.example.a22_2_sejong_project.program.ProgramFragment
import com.example.a22_2_sejong_project.utils.AddBoardArticleFragment
import com.example.a22_2_sejong_project.utils.BoardDetailFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener {
    private var _Binding: ActivityMainBinding? = null
    private val binding get() = _Binding!!
    var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _Binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

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
                supportFragmentManager.beginTransaction().replace(R.id.main_container_layout,BoardFragment())
                    .addToBackStack(null)
                    .commit()
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
                val frg = MyPageFragment()
                val uid = auth?.currentUser?.uid
                var bundle = Bundle()
                bundle.putString("uid",uid)
                frg.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.main_container_layout,frg).commit()
                binding.mainTitleTv.text = "내정보"
                return true
            }
        }
        return false
    }
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.main_container_layout,fragment).commit()
    }

    // 2초내 다시 클릭하면 앱 종료
    private var backPressedTime : Long = 0
    override fun onBackPressed() {
        Log.d("TAG", "뒤로가기")
        if (System.currentTimeMillis() - backPressedTime < 2000) {
            finish()
            return
        }
        // 처음 클릭 메시지
        Toast.makeText(this, "'뒤로가기' 버튼을 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }


}
