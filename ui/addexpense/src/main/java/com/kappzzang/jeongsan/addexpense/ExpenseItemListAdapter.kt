package com.kappzzang.jeongsan.addexpense

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappzzang.jeongsan.databinding.ItemExpenseItemBinding

class ExpenseItemListAdapter(
    private val onAddItem: () -> Unit,
    private val onRemoveItem: (Int) -> Unit
) : ListAdapter<ExpenseItemInput, ExpenseItemListAdapter.ExpenseItemViewHolder>(
    object :
        DiffUtil.ItemCallback<ExpenseItemInput>() {
        override fun areItemsTheSame(
            oldItem: ExpenseItemInput,
            newItem: ExpenseItemInput
        ): Boolean = oldItem === newItem

        override fun areContentsTheSame(
            oldItem: ExpenseItemInput,
            newItem: ExpenseItemInput
        ): Boolean = oldItem == newItem
    }
) {

    inner class ExpenseItemViewHolder(private val binding: ItemExpenseItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.expenseAddRemoveButton.setOnClickListener {
                val isLastItem = bindingAdapterPosition + 1 == bindingAdapter?.itemCount

                if (isLastItem) {
                    // TODO: 아이템 추가 시, 버튼의 모습이 올바르게 변하지 않음...
                    onAddItem()
                } else {
                    onRemoveItem(bindingAdapterPosition)
                }
            }
        }

        fun bind(item: ExpenseItemInput) {
            binding.item = item
            binding.isPlaceholder =
                (this.bindingAdapterPosition + 1 == this.bindingAdapter?.itemCount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseItemViewHolder {
        val viewHolderBinding =
            ItemExpenseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ExpenseItemViewHolder(viewHolderBinding)
        viewHolderBinding.viewHolder = viewHolder
        return viewHolder
    }

    override fun onBindViewHolder(holder: ExpenseItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}
