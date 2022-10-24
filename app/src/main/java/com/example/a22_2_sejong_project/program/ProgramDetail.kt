package com.example.a22_2_sejong_project.program

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.a22_2_sejong_project.R
import com.example.a22_2_sejong_project.databinding.ActivityAppLoginBinding
import com.example.a22_2_sejong_project.databinding.ActivityProgramDetailBinding
import org.jsoup.Jsoup

class ProgramDetail : AppCompatActivity() {
    private var _Binding: ActivityProgramDetailBinding? = null
    private val binding get() = _Binding!!

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        _Binding = ActivityProgramDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val url = intent.getStringExtra("href").toString()

        Thread(Runnable {
            val doc = Jsoup.connect(url).get()
            val imageUrl = 	"https://do.sejong.ac.kr" + doc.select("div.cover").attr("style")
                .replace("background-image:url(","")
                .replace(");","")

            this@ProgramDetail.runOnUiThread {
                Glide.with(this).load(imageUrl).into(binding.programDetailMainIv)
                binding.programDetailFromTv.text = doc.select("div.info").select("div.department").text().replace(" "," - ")
                binding.programDetailTitleTv.text = doc.select("div.title").select("h4").text()
                binding.pdTargetTv.text = "모집대상 : " + doc.select("li.target").select("span").text()
                binding.pdGradeTv.text = "학년/성별 : " +  doc.select("div.title").select("ul").select("span")[1].text()
                binding.pdMajorTv.text = "학과 : " + doc.select("div.title").select("ul").select("span")[2].text()
                binding.pdTimeTv.text = "신청/마감일자 : " + doc.select("div.form").select("p")[2].text() + "\n" +
                        "운영일자 : " + doc.select("div.form").select("p")[0].text()
                doc.select("div.description div[data-role=wysiwyg-content]").first().children().forEach {
                    binding.pdContentTv.append(it.text() + "\n")
                }
                binding.pdContentTv.append("\n" + "현황 : " + doc.select("li.tbody").first().children()[2].text())
                binding.programDetailPb.visibility = View.INVISIBLE

                binding.pdCommentRv.apply {

                }
            }
        }).start()

        binding.pdCommentWriteBtn.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.comment_dialog, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
            mBuilder.show()

        }
        binding.programDetailBackBtn.setOnClickListener {
            finish()
        }
        binding.pdWebBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("${intent.getStringExtra("href").toString()}"))
            startActivity(intent)
        }
        binding.pdCommentRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CommentAdapter(context)

        }
    }
    override fun onDestroy() {
        _Binding = null
        super.onDestroy()
    }
}