package com.kappzzang.jeongsan.navigation

import android.content.Context
import android.content.Intent

interface LoginNavigator {
    fun navigateToLogin(packageContext: Context): Intent
}
