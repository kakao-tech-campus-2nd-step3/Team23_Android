package com.kappzzang.jeongsan.expensedetail

import com.kappzzang.jeongsan.model.ExpenseDetailItem

interface ExpenseDetailDelegator {
    fun saveExpenseDetail(
        modifiedDetails: List<ExpenseDetailItem>
    )
}
