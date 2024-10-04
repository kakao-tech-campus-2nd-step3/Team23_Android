package com.kappzzang.jeongsan.navigation

import android.content.Context
import android.content.Intent

interface AppNavigator {
    fun navigateToMainPage(packageContext: Context): Intent

    fun navigateToCreateGroup(packageContext: Context): Intent

    fun navigateToExpenseDetail(packageContext: Context): Intent

    fun navigateToExpenseList(packageContext: Context): Intent

    fun navigateToCamera(packageContext: Context): Intent

    fun navigateToLogin(packageContext: Context): Intent
}
