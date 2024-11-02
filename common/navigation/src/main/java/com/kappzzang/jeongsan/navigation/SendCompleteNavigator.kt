package com.kappzzang.jeongsan.navigation

import android.content.Context
import android.content.Intent

interface SendCompleteNavigator {
    fun navigateToSendComplete(packageContext: Context): Intent
}
