package com.kappzzang.jeongsan.ui.inviteinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappzzang.jeongsan.R
import com.kappzzang.jeongsan.databinding.ItemMemberInfoBinding
import com.kappzzang.jeongsan.domain.model.MemberItem
import com.kappzzang.jeongsan.ui.MemberAdapter

class MemberInfoAdapter :
    ListAdapter<MemberItem, MemberInfoAdapter.ViewHolder>(MemberInfoDiffUtil()) {
    class ViewHolder(private val binding: ItemMemberInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name = binding.memberLayout.profileNameTextview
        val inviteInfo = binding.inviteInfoTextview
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemMemberInfoBinding.inflate(inflater, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currItem = getItem(position)
        holder.name.text = currItem.name
        holder.inviteInfo.text =
            if (!currItem.isInvited) {
                "초대 중"
            } else {
                "초대 완료"
            }
    }
}