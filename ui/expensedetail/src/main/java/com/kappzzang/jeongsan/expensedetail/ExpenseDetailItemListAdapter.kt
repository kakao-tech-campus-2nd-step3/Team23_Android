package com.kappzzang.jeongsan.expensedetail

import android.content.Context
import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappzzang.jeongsan.expensedetail.databinding.ItemExpenseDetailItemBinding
import com.kappzzang.jeongsan.model.ExpenseDetailItem

interface ExpenseDetailItemCallback {
    fun onCheckedChange(view: CompoundButton, enable: Boolean)

    fun onSelectedQuantityChanged(text: Editable)
}

interface ExpenseDetailCallback {
    fun onCheckedChange(enable: Boolean, index: Int)

    fun onSelectedQuantityChanged(quantity: Int, index: Int)
}

class ExpenseDetailItemListAdapter(
    private val context: Context,
    private val callback: ExpenseDetailCallback
) : ListAdapter<ExpenseDetailItem, ExpenseDetailItemListAdapter.ExpenseDetailItemViewHolder>(
    object :
        DiffUtil.ItemCallback<ExpenseDetailItem>() {
        override fun areItemsTheSame(
            oldItem: ExpenseDetailItem,
            newItem: ExpenseDetailItem
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ExpenseDetailItem,
            newItem: ExpenseDetailItem
        ): Boolean = oldItem == newItem
    }
) {
    class ExpenseDetailItemViewHolder(private val binding: ItemExpenseDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ExpenseDetailItem) {
            binding.item = item
            binding.isPlaceholder =
                (this.bindingAdapterPosition + 1 == this.bindingAdapter?.itemCount)
        }
    }

    private fun createSpinnerAdapter(maxIndex: Int): ArrayAdapter<Int> =
        ArrayAdapter(context, R.layout.spinner_selected_quantity, (0..maxIndex).toList())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseDetailItemViewHolder {
        val viewHolderBinding =
            ItemExpenseDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ExpenseDetailItemViewHolder(viewHolderBinding)
        viewHolderBinding.viewHolder = viewHolder
        viewHolderBinding.autoCompleteTextview.setAdapter(createSpinnerAdapter(20))
        viewHolderBinding.itemCallback =
            object : ExpenseDetailItemCallback {
                override fun onCheckedChange(view: CompoundButton, enable: Boolean) {
                    callback.onCheckedChange(enable, viewHolder.bindingAdapterPosition)
                }

                override fun onSelectedQuantityChanged(text: Editable) {
                    val parsedQuantity = text.toString().toIntOrNull() ?: return
                    callback.onSelectedQuantityChanged(
                        parsedQuantity,
                        viewHolder.bindingAdapterPosition
                    )
                }
            }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ExpenseDetailItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}
