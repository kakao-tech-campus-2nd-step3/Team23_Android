package com.kappzzang.jeongsan.sendmessage.navigation

import android.content.Context
import android.content.Intent
import com.kappzzang.jeongsan.navigation.SendMessageNavigator
import com.kappzzang.jeongsan.sendmessage.SendMessageActivity

class SendMessageNavigatorImpl : SendMessageNavigator {
    override fun navigateToSendMessage(packageContext: Context): Intent =
        Intent(packageContext, SendMessageActivity::class.java)
}
