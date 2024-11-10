package com.kappzzang.jeongsan.expensedetail

import android.content.Context

object ButtonTextConverter {
    @JvmStatic
    fun convertCurrentStateToPrimaryButtonText(
        page: ExpenseDetailPage,
        isEditable: Boolean,
        context: Context
    ): String = when (page) {
        ExpenseDetailPage.EXPENSE_DETAIL -> {
            if (isEditable) {
                context.getString(R.string.expense_detail_submit)
            } else {
                context.getString(R.string.expense_detail_dismiss)
            }
        }

        ExpenseDetailPage.SELECTION_STATUS -> {
            if (isEditable) {
                context.getString(R.string.expense_detail_switch_to_pending)
            } else {
                context.getString(R.string.expense_detail_switch_to_ongoing)
            }
        }
    }

    @JvmStatic
    fun convertCurrentStateToSecondaryButtonText(
        page: ExpenseDetailPage,
        isEditable: Boolean,
        context: Context
    ): String = when (page) {
        ExpenseDetailPage.EXPENSE_DETAIL -> {
            context.getString(R.string.expense_detail_check_status)
        }

        ExpenseDetailPage.SELECTION_STATUS -> {
            if (isEditable) {
                context.getString(R.string.expense_detail_modify)
            } else {
                context.getString(R.string.expense_detail_check_payer_selection)
            }
        }
    }
}
