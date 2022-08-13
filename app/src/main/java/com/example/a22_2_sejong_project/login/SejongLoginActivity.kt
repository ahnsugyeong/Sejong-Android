package com.example.a22_2_sejong_project.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.a22_2_sejong_project.R
import com.example.a22_2_sejong_project.databinding.ActivitySejongLoginBinding
import org.jsoup.Connection
import org.jsoup.Jsoup

class SejongLoginActivity : AppCompatActivity() {
    private var _Binding: ActivitySejongLoginBinding? = null
    private val binding get() = _Binding!!
    val userAgent = R.string.clawling_userAgent.toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _Binding = ActivitySejongLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.loginVerifyBtn.setOnClickListener {
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
            val asd = Jsoup.connect("http://classic.sejong.ac.kr/userCertStatus.do")
                .timeout(3000)
                .method(Connection.Method.GET)
                .cookies(loginForm.cookies())
                .execute()

            Log.d("TAG",asd.body().toString())

            var dialogText = "교내 학생 인증에 실패했습니다."
            if (homePage.body().contains("접속자 정보")){
                dialogText = "교내 학생 인증에 성공했습니다."
            }

            Handler(Looper.getMainLooper()).postDelayed(Runnable() {
                run() {
                    val dialog = LoginDialog(this,dialogText)
                    dialog.showDialog()
                }
            },0)
        }.start()
    }
    override fun onDestroy() {
        _Binding = null
        super.onDestroy()
    }
}