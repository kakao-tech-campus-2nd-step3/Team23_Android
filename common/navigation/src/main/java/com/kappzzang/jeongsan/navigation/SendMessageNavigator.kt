package com.kappzzang.jeongsan.navigation

import android.content.Context
import android.content.Intent

interface SendMessageNavigator {
    fun navigateToSendMessage(packageContext: Context, groupId: String): Intent
    fun navigateToSendComplete(packageContext: Context, groupId: String): Intent
}
