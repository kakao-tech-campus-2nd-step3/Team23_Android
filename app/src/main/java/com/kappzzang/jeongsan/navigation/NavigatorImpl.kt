package com.kappzzang.jeongsan.navigation

import android.content.Context
import android.content.Intent
import com.kappzzang.jeongsan.addexpense.AddExpenseActivity
import com.kappzzang.jeongsan.camera.ReceiptCameraActivity
import com.kappzzang.jeongsan.creategroup.CreateGroupActivity
import com.kappzzang.jeongsan.expensedetail.ExpenseDetailActivity
import com.kappzzang.jeongsan.expenselist.ExpenseListActivity
import com.kappzzang.jeongsan.login.LoginActivity
import com.kappzzang.jeongsan.main.MainActivity
import javax.inject.Inject

class NavigatorImpl @Inject constructor(private val context: Context): AppNavigator {
    override fun navigateToMainPage(packageContext: Context): Intent =
        Intent(packageContext, MainActivity::class.java)

    override fun navigateToCreateGroup(packageContext: Context): Intent =
        Intent(packageContext, CreateGroupActivity::class.java)

    override fun navigateToExpenseDetail(packageContext: Context): Intent =
        Intent(packageContext, ExpenseDetailActivity::class.java)

    override fun navigateToExpenseList(packageContext: Context): Intent =
        Intent(packageContext, ExpenseListActivity::class.java)

    override fun navigateToCamera(packageContext: Context): Intent =
        Intent(packageContext, ReceiptCameraActivity::class.java)

    override fun navigateToLogin(packageContext: Context): Intent =
        Intent(packageContext, LoginActivity::class.java)

    override fun navigateToAddExpense(packageContext: Context): Intent =
        Intent(packageContext, AddExpenseActivity::class.java)
}
