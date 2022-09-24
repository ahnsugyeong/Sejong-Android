package com.example.a22_2_sejong_project.login

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.a22_2_sejong_project.databinding.ActivityAppLoginBinding
import com.example.a22_2_sejong_project.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {
    private var _Binding: ActivitySignupBinding? = null
    private val binding get() = _Binding!!
    var auth : FirebaseAuth? = null
    var db : FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _Binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        binding.signupStudentIdTv.text = "학번 : " + intent.getStringExtra("studentId").toString()
        binding.signupBackBtn.setOnClickListener { finish() }
        binding.signupBtn.setOnClickListener {
            signup()
        }

        binding.siginupCollegeBtn.setOnClickListener {
            val menuList =arrayOf("인문과학대학","사회과학대학","경영경제대학","호텔관광대학","자연과학대학","생명과학대학","전자정보공학대학","소프트웨어융합대학","공과대학","예체능대학")
            val dialog = AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
            dialog.setTitle("단과대학 선택").setItems(menuList, DialogInterface.OnClickListener { dialogInterface, i ->
                binding.signupCollegeTv.text = menuList[i]
                binding.signupMajorTv.text = "전공 선택"
            })
            dialog.show()
        }
        binding.signupMajorBtn.setOnClickListener {
            var majorList = arrayOf<String>()
            if (binding.signupCollegeTv.text.toString().equals("인문과학대학")) {
                majorList = arrayOf("국어국문학과","국제학부","역사학과","교육학과")
            } else if (binding.signupCollegeTv.text.toString().equals("사회과학대학")) {
                majorList = arrayOf("행정학과","미디어커뮤니케이션학과")
            }else if (binding.signupCollegeTv.text.toString().equals("경영경제대학")) {
                majorList = arrayOf("경제학과","경영학부")
            }else if (binding.signupCollegeTv.text.toString().equals("호텔관광대학")) {
                majorList = arrayOf("호텔관광외식경영학부","호텔외식관광프랜차이즈경영학과","호텔외식비즈니스학과","글로벌조리학과")
            }else if (binding.signupCollegeTv.text.toString().equals("자연과학대학")) {
                majorList = arrayOf("수학통계학부","물리천문학과","화학과")
            }else if (binding.signupCollegeTv.text.toString().equals("생명과학대학")) {
                majorList = arrayOf("생명시스템학부","스마트생명산업융합학과")
            }else if (binding.signupCollegeTv.text.toString().equals("전자정보공학대학")) {
                majorList = arrayOf("전자정보통신공학과")
            }else if (binding.signupCollegeTv.text.toString().equals("소프트웨어융합대학")) {
                majorList = arrayOf("컴퓨터공학과","소프트웨어학과","정보보호학과","데이터사이언스학과","지능기전공학부","창의소프트학부","인공지능학과")
            }else if (binding.signupCollegeTv.text.toString().equals("공과대학")) {
                majorList = arrayOf("건축공학부","건설환경공학과","환경에너지공간융합학과","지구자원시스템공학과","기계항공우주공학부","나노신소재공학과","양자원자력공학과","국방시스템공학과","항공시스템공학과")
            }else if (binding.signupCollegeTv.text.toString().equals("예체능대학")) {
                majorList = arrayOf("회화과","패션디자인학과","음악과","체육학과","무용과","영화예술학과")
            }
            if (majorList.isNotEmpty()) {
                val dialog = AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
                dialog.setTitle("전공 선택").setItems(majorList, DialogInterface.OnClickListener { dialogInterface, i ->
                    binding.signupMajorTv.text = majorList[i]
                })
                dialog.show()
            } else {
                Toast.makeText(this,"단과대학을 선택해주세요.",Toast.LENGTH_SHORT).show()
            }

        }
    }

    // Firebase 회원 등록(email&비밀번호)
    fun signup() {
        if (binding.signupInputEmail.text.toString().isNotEmpty()
            && binding.signupInputPassword.text.toString().isNotEmpty() && binding.signupInputPasswordRe.text.toString().isNotEmpty()
            && binding.signupInputName.text.toString().isNotEmpty() && binding.signupInputNickname.text.toString().isNotEmpty()
            && binding.signupInputPassword.text.toString().equals(binding.signupInputPasswordRe.text.toString())
        ) {
            if (binding.signupCollegeTv.text.toString().equals("단과대학 선택") || binding.signupMajorTv.text.toString().equals("전공 선택")) {
                Toast.makeText(this,"단과대학 및 전공을 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                auth!!.createUserWithEmailAndPassword(binding.signupInputEmail.text.toString(), binding.signupInputPassword.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            userInfoResgister()
                            Toast.makeText(this,"회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        } else if(task.exception?.message.isNullOrEmpty()){
                            Toast.makeText(this,"회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        } else {
            Toast.makeText(this,"이메일 및 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun userInfoResgister() {
        var stdId = binding.signupStudentIdTv.text.toString().replace("학번 : ","")
        val data = hashMapOf(
            "uid" to auth?.uid.toString(),
            "stdId" to stdId,
            "email" to binding.signupInputEmail.text.toString(),
            "name" to binding.signupInputName.text.toString(),
            "nickname" to binding.signupInputNickname.text.toString(),
            "college" to binding.signupCollegeTv.text.toString(),
            "major" to binding.signupMajorTv.text.toString(),
            "note" to null,
            "profileUrl" to null
        )
        db?.collection("user")?.add(data)
            ?.addOnSuccessListener {
                Log.d("TAG","유저정보 등록 성공")
            }
            ?.addOnFailureListener {
                Log.d("TAG","유저정보 등록 실패")
            }
    }

    override fun onDestroy() {
        _Binding = null
        super.onDestroy()
    }
}