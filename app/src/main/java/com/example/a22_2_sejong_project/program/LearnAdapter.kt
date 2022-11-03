package com.example.a22_2_sejong_project.program

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a22_2_sejong_project.DTO.ProgramCommentDTO
import com.example.a22_2_sejong_project.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_program_list.view.*
import org.jsoup.select.Elements
import kotlin.math.log

class LearnAdapter(val context: Context, val programList: Elements) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val url = "https://do.sejong.ac.kr"

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_program_list, viewGroup, false)

        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = (holder as CustomViewHolder).itemView

        view.program_list_title_tv.text =
            programList.select("li")[position].select("b.title").text()
        view.program_list_time_1.text = programList.select("li")[position].select("small")[2].text()
        view.program_list_time_2.text = programList.select("li")[position].select("small")[3].text()
        Glide.with(context).load(
            url + programList.select("li div.cover")[position].attr("style")
                .replace("background-image:url(", "")
                .replace(");", "")
        ).into(view.program_list_iv)

        view.program_list_layout.setOnClickListener {
            val intent = Intent(context, ProgramDetail::class.java)
            intent.putExtra("href", programList.select("li a")[position].attr("abs:href"))
            context.startActivity(intent)
        }
    }
    inner class CustomViewHolder(var view: View) : RecyclerView.ViewHolder(view)
    override fun getItemCount() = programList.select("li").size
}