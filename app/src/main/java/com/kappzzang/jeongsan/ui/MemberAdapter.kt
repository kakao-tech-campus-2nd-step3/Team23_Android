package com.kappzzang.jeongsan.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kappzzang.jeongsan.R

class MemberAdapter(val memberList: List<Member>, val inflater: LayoutInflater, val layoutId: Int) :
    RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {
    inner class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.profile_name_textview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view = inflater.inflate(layoutId, parent, false)
        return MemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.name.text = memberList[position].name
    }

    override fun getItemCount(): Int = memberList.size
}
