package com.kappzzang.jeongsan.ui.expenselist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kappzzang.jeongsan.databinding.ItemExpenseBinding
import com.kappzzang.jeongsan.domain.model.ExpenseItem

class ExpenseListAdapter(private val expenseItemList: List<ExpenseItem>) :
    RecyclerView.Adapter<ExpenseListAdapter.MyViewHolder>() {

    inner class MyViewHolder(private val binding: ItemExpenseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(expenseItem: ExpenseItem) {
            binding.categoryColorView.setBackgroundColor(
                android.graphics.Color.parseColor(
                    expenseItem.categoryColor
                )
            )
            binding.expenseItem = expenseItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(
        ItemExpenseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int = expenseItemList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(expenseItemList[position])
    }
}
