package com.kappzzang.jeongsan.creategroup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kappzzang.jeongsan.creategroup.databinding.ItemMemberInviteBinding
import com.kappzzang.jeongsan.data.MemberUIData

class MemberAdapter() : ListAdapter<MemberUIData, MemberAdapter.MemberViewHolder>(diffUtil) {

    inner class MemberViewHolder(private val binding: ItemMemberInviteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(memberUIData: MemberUIData) {
            binding.profileNameTextview.text = memberUIData.name
            Glide.with(binding.root)
                .load(memberUIData.profileImageUrl)
                .circleCrop()
                .into(binding.profileImageImageview)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder =
        MemberViewHolder(
            ItemMemberInviteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MemberUIData>() {
            override fun areItemsTheSame(oldItem: MemberUIData, newItem: MemberUIData): Boolean =
                oldItem.uuid == newItem.uuid

            override fun areContentsTheSame(oldItem: MemberUIData, newItem: MemberUIData): Boolean =
                oldItem == newItem
        }
    }
}
