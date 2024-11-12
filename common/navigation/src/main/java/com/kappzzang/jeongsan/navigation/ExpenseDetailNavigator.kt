package com.kappzzang.jeongsan.navigation

import android.content.Context
import android.content.Intent
import com.kappzzang.jeongsan.model.ExpenseState

interface ExpenseDetailNavigator {
    fun navigateToExpenseDetail(
        packageContext: Context,
        expenseId: String,
        groupId: String,
        expenseState: ExpenseState,
        isPayer: Boolean
    ): Intent
}
