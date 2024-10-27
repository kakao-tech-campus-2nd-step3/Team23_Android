package com.kappzzang.jeongsan.expenselist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappzzang.jeongsan.data.ExpenseUiItem
import com.kappzzang.jeongsan.data.ListViewItemPositionInfo
import com.kappzzang.jeongsan.expenselist.databinding.ItemExpenseBinding
import com.kappzzang.jeongsan.util.DateConverter.formatToExpenseDate

class ExpenseListAdapter(private val onExpenseItemClickListener: (expenseId: String) -> Unit) :
    ListAdapter<ExpenseUiItem, ExpenseListAdapter.MyViewHolder>(
        object :
            DiffUtil.ItemCallback<ExpenseUiItem>() {
            override fun areItemsTheSame(oldItem: ExpenseUiItem, newItem: ExpenseUiItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ExpenseUiItem, newItem: ExpenseUiItem): Boolean =
                oldItem == newItem
        }
    ) {

    inner class MyViewHolder(
        private val binding: ItemExpenseBinding,
        private val onExpenseItemClickListener: (expenseId: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onExpenseItemClickListener.invoke(binding.expenseItem?.id ?: "")
            }
        }

        fun bind(expenseItem: ExpenseUiItem) {
            binding.categoryColorView.setBackgroundColor(
                android.graphics.Color.parseColor(
                    expenseItem.categoryColor
                )
            )
            binding.expenseItem = expenseItem
            binding.expenseDate = expenseItem.date.formatToExpenseDate()
            binding.positionInfo = ListViewItemPositionInfo(
                isFirstItem = this.bindingAdapterPosition == 0,
                isLastItem = this.bindingAdapter?.itemCount?.minus(1) == this.bindingAdapterPosition
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(
        ItemExpenseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        onExpenseItemClickListener
    )

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}
