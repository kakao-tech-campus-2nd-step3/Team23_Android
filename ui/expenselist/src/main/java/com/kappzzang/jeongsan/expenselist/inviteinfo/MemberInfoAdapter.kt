package com.kappzzang.jeongsan.expenselist.inviteinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        fun bind(memberItem: MemberItem) {
            Glide.with(binding.root)
                .load(memberItem.profileImageUrl)
                .circleCrop()
                .into(binding.profileImageImageview)
            binding.profileNameTextview.text = memberItem.name
            binding.sendInviteButton.isEnabled = !memberItem.isInvited

            if (!memberItem.isInvited) {
                binding.inviteInfoTextview.text = binding.root.context.getString(
                    com.kappzzang.jeongsan.expenselist.R.string.item_member_info_pending
                )
                binding.sendInviteButton.setOnClickListener {
                    sendMessageClickListener(memberItem.id)
                }
            } else {
                binding.inviteInfoTextview.text = binding.root.context.getString(
                    com.kappzzang.jeongsan.expenselist.R.string.item_member_info_complete
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemMemberInfoBinding.inflate(inflater, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
