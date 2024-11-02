package com.kappzzang.jeongsan.navigation

import android.content.Context
import android.content.Intent

interface ExpenseListNavigator {
    fun navigateToExpenseList(packageContext: Context, groupId: String): Intent
}
