package com.example.a22_2_sejong_project.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a22_2_sejong_project.R
import com.example.a22_2_sejong_project.program.ProgramDetail
import kotlinx.android.synthetic.main.item_home_dodream.view.*
import org.jsoup.select.Elements

class DodreamRvAdapter(val context: Context, val somenailList : Elements, val posterUrl : Elements, val href : Elements) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val url = "https://do.sejong.ac.kr"
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_home_dodream,viewGroup,false)

        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = (holder as CustomViewHolder).itemView

        view.item_home_dodream_from_tv.text = somenailList.select("div.department")[position].text().split("대학")[0] + "대학"
        view.item_home_dodream_title_tv.text = somenailList.select("b.title")[position*2+1].text()
        view.item_home_dodream_deadline_tv.text = "마감 : " + somenailList[position*2].select("span.date_title")[0]
            .nextElementSibling()
            .nextElementSibling().text()
        Glide.with(context)
            .load(url + posterUrl[position]
            .attr("style")
            .replace("background-image:url(","")
            .replace(");",""))
            .into(view.item_home_dodream_iv)
        view.home_dodream_somenail_layout.setOnClickListener {
            val intent = Intent(context,ProgramDetail::class.java)
            intent.putExtra("href",href[position].attr("abs:href"))
            context.startActivity(intent)
        }
    }
    inner class CustomViewHolder(var view : View) : RecyclerView.ViewHolder(view)
    override fun getItemCount() = 5
}