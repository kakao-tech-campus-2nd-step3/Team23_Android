package com.kappzzang.jeongsan.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kappzzang.jeongsan.databinding.ItemMainDoneTitleBinding
import com.kappzzang.jeongsan.databinding.ItemMainGroupBinding
import com.kappzzang.jeongsan.databinding.ItemMainProgressTitleBinding
import com.kappzzang.jeongsan.expenselist.ExpenseListActivity

class GroupListAdapter : ListAdapter<GroupViewItem, RecyclerView.ViewHolder>(diffUtil) {

    inner class ProgressTitleViewHolder(binding: ItemMainProgressTitleBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class DoneTitleViewHolder(binding: ItemMainDoneTitleBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class GroupViewHolder(private val binding: ItemMainGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            // TODO: 이후 Jetpack Navigation을 사용하여 화면 전환
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, com.kappzzang.jeongsan.expenselist.ExpenseListActivity::class.java)
                currentList[bindingAdapterPosition].let { currentViewItem ->
                    if (currentViewItem is GroupViewItem.Group) {
                        intent.putExtra("groupId", currentViewItem.groupItem.id)
                    }
                }
                startActivity(binding.root.context, intent, null)
            }
        }

        fun bind(groupViewItem: GroupViewItem) {
            binding.groupItem = (groupViewItem as GroupViewItem.Group).groupItem

            val profileImageViewList = listOf(
                binding.profile0Imageview,
                binding.profile1Imageview,
                binding.profile2Imageview
            )

            for (i in 0..2) {
                if (i < groupViewItem.groupItem.profileImageURL.size) {
                    Glide.with(binding.root)
                        .load(groupViewItem.groupItem.profileImageURL[i])
                        .circleCrop()
                        .into(profileImageViewList[i])
                } else {
                    profileImageViewList[i].isVisible = false
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            ViewType.PROGRESS_TITLE -> {
                val binding = ItemMainProgressTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ProgressTitleViewHolder(binding)
            }

            ViewType.DONE_TITLE -> {
                val binding = ItemMainDoneTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                DoneTitleViewHolder(binding)
            }

            ViewType.GROUP -> {
                val binding =
                    ItemMainGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GroupViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (currentList[position] is GroupViewItem.Group) {
            (holder as GroupViewHolder).bind(currentList[position])
        }
    }

    override fun getItemViewType(position: Int): Int = when (currentList[position]) {
        is GroupViewItem.ProgressTitle -> ViewType.PROGRESS_TITLE
        is GroupViewItem.DoneTitle -> ViewType.DONE_TITLE
        is GroupViewItem.Group -> ViewType.GROUP
    }

    object ViewType {
        const val PROGRESS_TITLE = 0
        const val DONE_TITLE = 1
        const val GROUP = 2
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<GroupViewItem>() {
            override fun areItemsTheSame(oldItem: GroupViewItem, newItem: GroupViewItem): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: GroupViewItem,
                newItem: GroupViewItem
            ): Boolean = oldItem == newItem
        }
    }
}
