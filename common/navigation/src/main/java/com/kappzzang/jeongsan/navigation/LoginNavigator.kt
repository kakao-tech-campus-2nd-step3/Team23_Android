package com.kappzzang.jeongsan.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri

interface LoginNavigator {
    fun login(packageContext: Context): Intent

    fun loginAndEnterGroup(packageContext: Context, inviteGroupId: String): Intent

    fun loginAndEnterDetailExpense(
        packageContext: Context,
        groupId: String,
        expenseId: String,
    ): Intent
}
