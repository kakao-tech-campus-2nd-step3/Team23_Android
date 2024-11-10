package com.kappzzang.jeongsan.login.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.kappzzang.jeongsan.login.LoginActivity
import com.kappzzang.jeongsan.navigation.LoginNavigator
import javax.inject.Inject

class LoginNavigatorImpl @Inject constructor() : LoginNavigator {
    override fun login(packageContext: Context): Intent =
        Intent(packageContext, LoginActivity::class.java)

    override fun loginAndEnterGroup(packageContext: Context, inviteGroupId: String): Intent {
        val inviteGroupUri = Uri.parse("jeongsan://inviteGroup/").buildUpon()
            .appendQueryParameter("groupId", inviteGroupId)
            .build()
        return Intent(packageContext, LoginActivity::class.java).apply {
            data = inviteGroupUri
        }
    }

    override fun loginAndEnterDetailExpense(
        packageContext: Context,
        groupId: String,
        expenseId: String
    ): Intent {
        val newExpenseUri = Uri.parse("jeongsan://newExpense/").buildUpon()
            .appendQueryParameter("groupId", groupId)
            .appendQueryParameter("expenseId", expenseId)
            .build()
        return Intent(packageContext, LoginActivity::class.java).apply {
            data = newExpenseUri
        }
    }
}
