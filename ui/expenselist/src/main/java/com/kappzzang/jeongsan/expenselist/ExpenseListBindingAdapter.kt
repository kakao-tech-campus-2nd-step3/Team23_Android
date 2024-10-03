package com.kappzzang.jeongsan.expenselist

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.StateFlow

object ExpenseListBindingAdapter {
    @BindingAdapter("expenseItems")
    @JvmStatic
    fun attachExpenseList(
        recyclerView: RecyclerView,
        items: StateFlow<ExpenseListViewUIData>?
    ) {
        items?.let {
            (recyclerView.adapter as? ExpenseListAdapter)
                ?.submitList(
                    it.value.expenseItems
                )
        }
    }
}
