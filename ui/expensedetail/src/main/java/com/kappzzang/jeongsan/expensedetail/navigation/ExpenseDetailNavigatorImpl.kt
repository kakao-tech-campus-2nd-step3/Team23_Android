package com.kappzzang.jeongsan.expensedetail.navigation

import android.content.Context
import android.content.Intent
import com.kappzzang.jeongsan.expensedetail.ExpenseDetailActivity
import com.kappzzang.jeongsan.intentcontract.ExpenseDetailContract
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.navigation.ExpenseDetailNavigator
import javax.inject.Inject

class ExpenseDetailNavigatorImpl @Inject constructor() : ExpenseDetailNavigator {
    override fun navigateToExpenseDetail(
        packageContext: Context,
        expenseId: String,
        groupId: String,
        expenseState: ExpenseState,
        isPayer: Boolean
    ): Intent = Intent(packageContext, ExpenseDetailActivity::class.java)
        .apply {
            this.putExtra(ExpenseDetailContract.EXPENSE_ID, expenseId)
            this.putExtra(ExpenseDetailContract.GROUP_ID, groupId)
            this.putExtra(
                ExpenseDetailContract.EXPENSE_STATE,
                expenseState
            )
            this.putExtra(ExpenseDetailContract.IS_PAYER, isPayer)
        }
}
