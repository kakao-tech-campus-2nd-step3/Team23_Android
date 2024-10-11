package com.kappzzang.jeongsan.expenselist.sendmessage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kappzzang.jeongsan.expenselist.R
import com.kappzzang.jeongsan.expenselist.databinding.ItemMemberExpenseBinding
import com.kappzzang.jeongsan.model.TransferDetailItem
import com.kappzzang.jeongsan.util.IntegerFormatter.formatDecimalSeparator

class MemberAdapter : ListAdapter<TransferDetailItem, MemberAdapter.MemberViewHolder>(diffUtil) {

    inner class MemberViewHolder(private val binding: ItemMemberExpenseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transferDetailItem: TransferDetailItem) {
            binding.profileNameTextview.text = transferDetailItem.name
            binding.expenseTextview.text = transferDetailItem.fee.formatDecimalSeparator()
                .plus(binding.root.context.getString(R.string.send_message_money_unit))
            Glide.with(binding.root)
                .load(transferDetailItem.profileImageUrl)
                .circleCrop()
                .into(binding.profileImageImageview)
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
