package com.kappzzang.jeongsan.expensedetail.navigation

import android.content.Context
import android.content.Intent
import com.kappzzang.jeongsan.expensedetail.ExpenseDetailActivity
import com.kappzzang.jeongsan.intentcontract.ExpenseDetailContract
import com.kappzzang.jeongsan.navigation.ExpenseDetailNavigator
import javax.inject.Inject

class ExpenseDetailNavigatorImpl @Inject constructor() : ExpenseDetailNavigator {
    override fun navigateToExpenseDetail(
        packageContext: Context,
        expenseId: String,
        groupId: String,
        editable: Boolean,
        isPayer: Boolean,
    ): Intent = Intent(packageContext, ExpenseDetailActivity::class.java)
        .apply {
            this.putExtra(ExpenseDetailContract.EXPENSE_ID, expenseId)
            this.putExtra(ExpenseDetailContract.GROUP_ID, groupId)
            this.putExtra(
                ExpenseDetailContract.EDITABLE,
                editable
            )
            this.putExtra(ExpenseDetailContract.IS_PAYER, isPayer)
        }
}
