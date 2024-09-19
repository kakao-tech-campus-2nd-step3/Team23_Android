package com.kappzzang.jeongsan.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kappzzang.jeongsan.databinding.ItemMainDoneTitleBinding
import com.kappzzang.jeongsan.databinding.ItemMainGroupBinding
import com.kappzzang.jeongsan.databinding.ItemMainProgressTitleBinding
import com.kappzzang.jeongsan.domain.model.GroupItem
import com.kappzzang.jeongsan.domain.model.GroupViewType

class GroupListAdapter(private val groupItemList: List<GroupItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ProgressTitleViewHolder(binding: ItemMainProgressTitleBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class DoneTitleViewHolder(binding: ItemMainDoneTitleBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class GroupViewHolder(private val binding: ItemMainGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(groupItem: GroupItem) {
            binding.groupItem = groupItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            GroupViewType.PROGRESS_TITLE.ordinal -> {
                val binding = ItemMainProgressTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ProgressTitleViewHolder(binding)
            }

            GroupViewType.DONE_TITLE.ordinal -> {
                val binding = ItemMainDoneTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                DoneTitleViewHolder(binding)
            }

            else -> {
                val binding =
                    ItemMainGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GroupViewHolder(binding)
            }
        }

    override fun getItemCount(): Int = groupItemList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GroupViewHolder) {
            holder.bind(groupItemList[position])
        }
    }

    override fun getItemViewType(position: Int): Int = groupItemList[position].viewType.ordinal
}
