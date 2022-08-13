package com.example.a22_2_sejong_project.login

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import androidx.core.content.ContextCompat.startActivity
import com.example.a22_2_sejong_project.R
import kotlinx.android.synthetic.main.login_dialog.*

// 커스텀 다이어로그 편집
class LoginDialog(val context: Context, val str : String) {
    private val dialog = Dialog(context)

    fun showDialog() {
        dialog.setContentView(R.layout.login_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        dialog.sejong_login_dialog_tv.text = str.toString()

        dialog.sejong_login_dialog_btn.setOnClickListener {
            dialog.dismiss()
            if (str == "교내 학생 인증에 성공했습니다.") {
                context.startActivity(Intent(context,AppLoginActivity::class.java))
            }
        }
    }
}