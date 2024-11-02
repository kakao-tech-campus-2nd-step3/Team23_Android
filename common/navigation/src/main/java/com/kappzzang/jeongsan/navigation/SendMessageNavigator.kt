package com.kappzzang.jeongsan.navigation

import android.content.Context
import android.content.Intent

interface SendMessageNavigator {
    fun navigateToSendMessage(packageContext: Context): Intent
}
