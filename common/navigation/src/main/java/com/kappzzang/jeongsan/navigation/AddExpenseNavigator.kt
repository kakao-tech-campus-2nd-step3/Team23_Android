package com.kappzzang.jeongsan.navigation

import android.content.Context
import android.content.Intent

interface AddExpenseNavigator {
    fun navigateToAddExpense(packageContext: Context): Intent
}
