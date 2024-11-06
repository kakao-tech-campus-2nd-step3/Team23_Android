package com.kappzzang.jeongsan.addexpense.colorpicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappzzang.jeongsan.addexpense.databinding.ItemExpenseCategoryBinding
import com.kappzzang.jeongsan.data.ExpenseCategoryUIItem

class CategoryListAdapter(private val onCategoryItemClickListener: (categoryId: String) -> Unit) :
    ListAdapter<ExpenseCategoryUIItem, CategoryListAdapter.CategoryViewHolder>(
        object :
            DiffUtil.ItemCallback<ExpenseCategoryUIItem>() {
            override fun areItemsTheSame(
                oldItem: ExpenseCategoryUIItem,
                newItem: ExpenseCategoryUIItem
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ExpenseCategoryUIItem,
                newItem: ExpenseCategoryUIItem
            ): Boolean = oldItem == newItem
        }
    ) {

    inner class CategoryViewHolder(
        private val binding: ItemExpenseCategoryBinding,
        private val onExpenseItemClickListener: (expenseId: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onExpenseItemClickListener.invoke(binding.item?.id ?: "")
            }
        }

        fun bind(categoryItem: ExpenseCategoryUIItem) {
            binding.item = categoryItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(
            ItemExpenseCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onCategoryItemClickListener
        )

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}
