package com.example.a22_2_sejong_project.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.a22_2_sejong_project.databinding.ActivityAppLoginBinding
import com.example.a22_2_sejong_project.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    private var _Binding: ActivitySignupBinding? = null
    private val binding get() = _Binding!!
    var auth : FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _Binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.signupStudentIdTv.text = intent.getStringExtra("studentId").toString()

        binding.signupBtn.setOnClickListener {
            signup()
        }
    }
    // Firebase 회원 등록(email&비밀번호)
    fun signup() {
        if (binding.signupInputEmail.text.toString().length > 0&&binding.signupInputPassword.text.toString().length>0) {
            auth!!.createUserWithEmailAndPassword(binding.signupInputEmail.text.toString(), binding.signupInputPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,"회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    } else if(task.exception?.message.isNullOrEmpty()){
                        Toast.makeText(this,"회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this,"이메일을 입력하세요.", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroy() {
        _Binding = null
        super.onDestroy()
    }
}