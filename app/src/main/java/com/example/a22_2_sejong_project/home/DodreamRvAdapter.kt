package com.example.a22_2_sejong_project.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a22_2_sejong_project.R
import kotlinx.android.synthetic.main.item_home_dodream.view.*
import org.jsoup.select.Elements

class DodreamRvAdapter(val context: Context, val somenailList : Elements) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_home_dodream,viewGroup,false)


        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = (holder as CustomViewHolder).itemView

        view.item_home_dodream_from_tv.text = somenailList.select("span.small_department")[position*2+1].text()
        view.item_home_dodream_title_tv.text = somenailList.select("b.title")[position*2+1].text()
    }
    inner class CustomViewHolder(var view : View) : RecyclerView.ViewHolder(view)
    override fun getItemCount() = 5
}