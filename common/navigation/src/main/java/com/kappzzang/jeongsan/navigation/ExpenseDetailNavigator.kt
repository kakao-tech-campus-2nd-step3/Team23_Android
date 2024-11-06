package com.kappzzang.jeongsan.navigation

import android.content.Context
import android.content.Intent

interface ExpenseDetailNavigator {
    fun navigateToExpenseDetail(
        packageContext: Context,
        expenseId: String,
        groupId: String,
        editable: Boolean
    ): Intent
}
