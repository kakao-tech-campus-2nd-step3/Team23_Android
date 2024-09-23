package com.kappzzang.jeongsan.ui.expenselist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappzzang.jeongsan.databinding.ItemExpenseBinding
import com.kappzzang.jeongsan.domain.model.ExpenseItem
import java.text.SimpleDateFormat
import java.util.Locale

class ExpenseListAdapter :
    ListAdapter<ExpenseItem, ExpenseListAdapter.MyViewHolder>(
        object :
            DiffUtil.ItemCallback<ExpenseItem>() {
            override fun areItemsTheSame(
                oldItem: ExpenseItem,
                newItem: ExpenseItem
            ): Boolean = oldItem.id === newItem.id

            override fun areContentsTheSame(
                oldItem: ExpenseItem,
                newItem: ExpenseItem
            ): Boolean = oldItem == newItem
        }
    ) {

    inner class MyViewHolder(private val binding: ItemExpenseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(expenseItem: ExpenseItem) {
            binding.categoryColorView.setBackgroundColor(
                android.graphics.Color.parseColor(
                    expenseItem.categoryColor
                )
            )
            binding.expenseItem = expenseItem
            binding.expenseDate = SimpleDateFormat("MM/dd HH:mm", Locale.KOREAN).format(expenseItem.date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(
        ItemExpenseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}
