package com.kappzzang.jeongsan.expenselist

import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kappzzang.jeongsan.expenselist.customview.CustomOutlineProvider
import com.kappzzang.jeongsan.expenselist.customview.ExpenseListItemBoxType
import kotlinx.coroutines.flow.StateFlow

object ExpenseListBindingAdapter {
    @BindingAdapter("expenseItems")
    @JvmStatic
    fun attachExpenseList(
        recyclerView: RecyclerView,
        items: StateFlow<com.kappzzang.jeongsan.data.ExpenseListViewUIData>?
    ) {
        items?.let {
            (recyclerView.adapter as? ExpenseListAdapter)
                ?.submitList(
                    it.value.expenseItems
                )
        }
    }

    @BindingAdapter(
        value = ["clipRadius", "clipUpperCorner", "clipBottomCorner"],
        requireAll = false
    )
    @JvmStatic
    fun bindCorners(view: View, radius: Float?, upperCorner: Boolean?, bottomCorner: Boolean?) {
        val boxType = if (upperCorner == true) {
            if (bottomCorner == true) {
                ExpenseListItemBoxType.ALL_CORNERS
            } else {
                ExpenseListItemBoxType.TOP_CORNER
            }
        } else {
            if (bottomCorner == true) {
                ExpenseListItemBoxType.BOTTOM_CORNER
            } else {
                ExpenseListItemBoxType.NO_CORNERS
            }
        }

        view.outlineProvider = CustomOutlineProvider(
            radius ?: view.resources.getDimension(R.dimen.expense_list_item_corner_radius),
            boxType
        )
        view.clipToOutline = true
    }
}
