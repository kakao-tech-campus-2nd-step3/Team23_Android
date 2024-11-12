package com.kappzzang.jeongsan.expensedetail.expensedetailpage

import android.content.Context
import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappzzang.jeongsan.data.ExpenseDetailUIData
import com.kappzzang.jeongsan.expensedetail.R
import com.kappzzang.jeongsan.expensedetail.databinding.ItemExpenseDetailItemBinding

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
) : ListAdapter<ExpenseDetailUIData, ExpenseDetailItemListAdapter.ExpenseDetailItemViewHolder>(
    object :
        DiffUtil.ItemCallback<ExpenseDetailUIData>() {
        override fun areItemsTheSame(
            oldItem: ExpenseDetailUIData,
            newItem: ExpenseDetailUIData
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ExpenseDetailUIData,
            newItem: ExpenseDetailUIData
        ): Boolean = oldItem == newItem
    }
) {
    class ExpenseDetailItemViewHolder(private val binding: ItemExpenseDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ExpenseDetailUIData) {
            binding.item = item
            binding.isPlaceholder =
                (this.bindingAdapterPosition + 1 == this.bindingAdapter?.itemCount)
            (binding.autoCompleteTextview.adapter as? ArrayAdapter<Int>)?.let {
                if (it.count > item.itemQuantity + 1) {
                    (item.itemQuantity + 1 until it.count).forEach { num ->
                        it.remove(num)
                    }
                } else if (it.count <= item.itemQuantity) {
                    (it.count..item.itemQuantity).forEach { num ->
                        it.add(num)
                    }
                }
            }
        }
    }

    private fun createSpinnerAdapter(): ArrayAdapter<Int> =
        ArrayAdapter(context, R.layout.spinner_selected_quantity, mutableListOf<Int>())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseDetailItemViewHolder {
        val viewHolderBinding =
            ItemExpenseDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ExpenseDetailItemViewHolder(viewHolderBinding)
        viewHolderBinding.autoCompleteTextview.setAdapter(createSpinnerAdapter())
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
