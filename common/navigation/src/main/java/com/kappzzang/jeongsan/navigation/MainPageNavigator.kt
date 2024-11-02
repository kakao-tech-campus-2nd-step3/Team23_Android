package com.kappzzang.jeongsan.navigation

import android.content.Context
import android.content.Intent

interface MainPageNavigator {
    fun navigateToMainPage(packageContext: Context): Intent
}
