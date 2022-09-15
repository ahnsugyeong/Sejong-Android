package com.example.a22_2_sejong_project.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.a22_2_sejong_project.MainActivity
import com.example.a22_2_sejong_project.databinding.ActivityAppLoginBinding
import com.google.firebase.auth.FirebaseAuth

class AppLoginActivity : AppCompatActivity() {
    private var auth : FirebaseAuth? = null
    private var _Binding: ActivityAppLoginBinding? = null
    private val binding get() = _Binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _Binding = ActivityAppLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.appSignupBtn.setOnClickListener {
            val studentId = intent.getStringExtra("studentId").toString()
            val intent = Intent(this,SignupActivity::class.java)
            intent.putExtra("studentId",studentId)
            Log.d("TAG",studentId)
            startActivity(intent)
        }
        binding.appLoginBtn.setOnClickListener {
            signin()
        }
    }
    // Firebase 로그인 검사
    fun signin() {
        if (binding.loginInputEmail.text.toString().isNotEmpty() && binding.loginInputPassword.text.toString().isNotEmpty()) {
            auth!!.signInWithEmailAndPassword(binding.loginInputEmail.text.toString(), binding.loginInputPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        auth!!.currentUser?.apply {
                            val intent = Intent(applicationContext,MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                        }
                        Toast.makeText(this, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "이메일 및 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        else {
            Toast.makeText(this, "이메일 및 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroy() {
        _Binding = null
        super.onDestroy()
    }
}