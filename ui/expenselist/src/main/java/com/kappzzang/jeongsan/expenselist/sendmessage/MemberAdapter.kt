package com.kappzzang.jeongsan.expenselist.sendmessage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappzzang.jeongsan.expenselist.databinding.ItemMemberExpenseBinding
import com.kappzzang.jeongsan.model.TransferDetailItem

class MemberAdapter : ListAdapter<TransferDetailItem, MemberAdapter.MemberViewHolder>(diffUtil) {

    inner class MemberViewHolder(private val binding: ItemMemberExpenseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transferDetailItem: TransferDetailItem) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder =
        MemberViewHolder(
            ItemMemberExpenseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<TransferDetailItem>() {
            override fun areItemsTheSame(
                oldItem: TransferDetailItem,
                newItem: TransferDetailItem
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: TransferDetailItem,
                newItem: TransferDetailItem
            ): Boolean = oldItem == newItem
        }
    }
}
