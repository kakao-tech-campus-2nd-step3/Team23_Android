package com.kappzzang.jeongsan.expenselist.navigation

import android.content.Context
import android.content.Intent
import com.kappzzang.jeongsan.expenselist.ExpenseListActivity
import com.kappzzang.jeongsan.intentcontract.ExpenseListContract
import com.kappzzang.jeongsan.navigation.ExpenseListNavigator

class ExpenseListNavigatorImpl:ExpenseListNavigator {
    override fun navigateToExpenseList(packageContext: Context, groupId: String): Intent {
        val intent = Intent(packageContext, ExpenseListActivity::class.java)
        intent.putExtra(ExpenseListContract.GROUP_ID, groupId)

        return intent
    }
}
