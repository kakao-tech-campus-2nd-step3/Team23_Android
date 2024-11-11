package com.kappzzang.jeongsan.expensedetail

import android.content.Context
import com.kappzzang.jeongsan.model.ExpenseState

object ButtonTextConverter {
    @JvmStatic
    fun convertCurrentStateToPrimaryButtonText(
        page: ExpenseDetailPage,
        expenseState: ExpenseState,
        context: Context
    ): String = when (page) {
        ExpenseDetailPage.EXPENSE_DETAIL -> {
            if (expenseState.editable()) {
                context.getString(R.string.expense_detail_submit)
            } else {
                context.getString(R.string.expense_detail_dismiss)
            }
        }

        ExpenseDetailPage.SELECTION_STATUS -> {
            when (expenseState) {
                ExpenseState.CONFIRMED, ExpenseState.NOT_CONFIRMED ->
                    context.getString(R.string.expense_detail_switch_to_pending)

                ExpenseState.TRANSFER_PENDING ->
                    context.getString(R.string.expense_detail_switch_to_ongoing)

                ExpenseState.TRANSFERED ->
                    context.getString(R.string.expense_detail_dismiss)
            }
        }
    }

    @JvmStatic
    fun convertCurrentStateToSecondaryButtonText(
        page: ExpenseDetailPage,
        expenseState: ExpenseState,
        context: Context
    ): String = when (page) {
        ExpenseDetailPage.EXPENSE_DETAIL -> {
            context.getString(R.string.expense_detail_check_status)
        }

        ExpenseDetailPage.SELECTION_STATUS -> {
            if (expenseState.editable()) {
                context.getString(R.string.expense_detail_modify)
            } else {
                context.getString(R.string.expense_detail_check_payer_selection)
            }
        }
    }
}
