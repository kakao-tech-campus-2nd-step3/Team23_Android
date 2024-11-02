package com.kappzzang.jeongsan.expensedetail.navigation

import android.content.Context
import android.content.Intent
import com.kappzzang.jeongsan.expensedetail.ExpenseDetailActivity
import com.kappzzang.jeongsan.navigation.ExpenseDetailNavigator

class ExpenseDetailNavigatorImpl: ExpenseDetailNavigator {
    override fun navigateToExpenseDetail(packageContext: Context): Intent
            = Intent(packageContext, ExpenseDetailActivity::class.java)

}
