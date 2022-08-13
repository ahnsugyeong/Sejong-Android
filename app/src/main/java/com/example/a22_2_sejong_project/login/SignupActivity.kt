package com.example.a22_2_sejong_project.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a22_2_sejong_project.databinding.ActivityAppLoginBinding
import com.example.a22_2_sejong_project.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private var _Binding: ActivitySignupBinding? = null
    private val binding get() = _Binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _Binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
    override fun onDestroy() {
        _Binding = null
        super.onDestroy()
    }
}