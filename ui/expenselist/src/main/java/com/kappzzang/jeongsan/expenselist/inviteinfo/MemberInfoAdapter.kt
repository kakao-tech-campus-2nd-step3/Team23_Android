package com.kappzzang.jeongsan.expenselist.inviteinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappzzang.jeongsan.R
import com.kappzzang.jeongsan.expenselist.databinding.ItemMemberInfoBinding
import com.kappzzang.jeongsan.model.MemberItem

class MemberInfoAdapter(private val sendMessageClickListener: (String) -> Unit) :
    ListAdapter<MemberItem, MemberInfoAdapter.ViewHolder>(
        MemberInfoDiffUtil()
    ) {
    inner class ViewHolder(private val binding: ItemMemberInfoBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        fun bind(id: String) {
            binding.sendInviteButton.setOnClickListener {
                sendMessageClickListener(id)
            }
        }
        val name: TextView =
            binding.memberLayout.findViewById(R.id.profile_name_textview)
        val inviteInfo = binding.inviteInfoTextview
        val inviteButton = binding.sendInviteButton
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
                holder.bind(currItem.id)
                holder.inviteButton.isEnabled = true
                holder.itemView.context.getString(
                    com.kappzzang.jeongsan.expenselist.R.string.item_member_info_pending
                )
            } else {
                holder.inviteButton.isEnabled = false
                holder.itemView.context.getString(
                    com.kappzzang.jeongsan.expenselist.R.string.item_member_info_complete
                )
            }
    }
}
