package com.kappzzang.jeongsan.navigation

import android.content.Intent

interface AppNavigator {
    fun navigateToMainPage(): Intent

    fun navigateToCreateGroup(): Intent

    fun navigateToExpenseDetail(): Intent

    fun navigateToExpenseList(): Intent

    fun navigateToCamera(): Intent

    fun navigateToLogin(): Intent
}
