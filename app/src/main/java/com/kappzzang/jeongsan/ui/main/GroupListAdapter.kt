package com.kappzzang.jeongsan.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.kappzzang.jeongsan.databinding.ItemMainDoneTitleBinding
import com.kappzzang.jeongsan.databinding.ItemMainGroupBinding
import com.kappzzang.jeongsan.databinding.ItemMainProgressTitleBinding
import com.kappzzang.jeongsan.ui.expenselist.ExpenseListActivity

class GroupListAdapter(private val groupItemList: List<GroupViewItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ProgressTitleViewHolder(binding: ItemMainProgressTitleBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class DoneTitleViewHolder(binding: ItemMainDoneTitleBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class GroupViewHolder(private val binding: ItemMainGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            // TODO: 임시 연결용 코드
            binding.root.setOnClickListener {
                startActivity(
                    binding.root.context,
                    Intent(binding.root.context, ExpenseListActivity::class.java),
                    null
                )
            }
        }

        fun bind(groupViewItem: GroupViewItem) {
            binding.groupItem = (groupViewItem as GroupViewItem.Group).groupItem
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

    override fun getItemCount(): Int = groupItemList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (groupItemList[position] is GroupViewItem.Group) {
            (holder as GroupViewHolder).bind(groupItemList[position])
        }
    }

    override fun getItemViewType(position: Int): Int = when (groupItemList[position]) {
        is GroupViewItem.ProgressTitle -> ViewType.PROGRESS_TITLE
        is GroupViewItem.DoneTitle -> ViewType.DONE_TITLE
        is GroupViewItem.Group -> ViewType.GROUP
    }

    object ViewType {
        const val PROGRESS_TITLE = 0
        const val DONE_TITLE = 1
        const val GROUP = 2
    }
}
