package com.example.a22_2_sejong_project.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.example.a22_2_sejong_project.MainActivity
import com.example.a22_2_sejong_project.R
import com.example.a22_2_sejong_project.databinding.ActivitySejongLoginBinding
import com.google.firebase.auth.FirebaseAuth
import org.jsoup.Connection
import org.jsoup.Jsoup

class SejongLoginActivity : AppCompatActivity() {
    private var _Binding: ActivitySejongLoginBinding? = null
    private val binding get() = _Binding!!
    private var auth : FirebaseAuth? = null
    val userAgent = R.string.clawling_userAgent.toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _Binding = ActivitySejongLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.loginVerifyBtn.setOnClickListener {
            binding.sejongLoginPr.visibility = View.VISIBLE
            sejongVerify()
        }
    }
    // 세종대학교 대양휴머니티칼리지 Jsoup lib 이용 크롤링
    fun sejongVerify() {
        val data = HashMap<String,String>()
        data["userId"] = binding.loginInputStdId.text.toString()
        data["password"] = binding.loginInputStdPassword.text.toString()

        Thread {
            val loginForm  = Jsoup.connect("http://classic.sejong.ac.kr/userLoginPage.do")
                .timeout(3000)
                .method(Connection.Method.GET)
                .execute()

            val homePage = Jsoup.connect("http://classic.sejong.ac.kr/userLogin.do")
                .timeout(3000)
                .data(data)
                .method(Connection.Method.POST)
                .userAgent(userAgent)
                .cookies(loginForm.cookies())
                .execute()

            var dialogText = "교내 학생 인증에 실패했습니다."
            if (homePage.body().contains("접속자 정보")){
                dialogText = "교내 학생 인증에 성공했습니다."
            }

            Handler(Looper.getMainLooper()).postDelayed(Runnable() {
                run() {
                    val studentId = binding.loginInputStdId.text.toString()
                    val dialog = LoginDialog(this,dialogText,studentId)
                    dialog.showDialog()
                }
            },0)
        }.start()
        binding.sejongLoginPr.visibility = View.GONE
    }
    override fun onStart() {
        super.onStart()
        auth!!.currentUser?.apply {
            startActivity(Intent(applicationContext,MainActivity::class.java))
            finish()
        }
    }
    override fun onDestroy() {
        _Binding = null
        super.onDestroy()
    }
}