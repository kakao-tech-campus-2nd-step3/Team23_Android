package com.kappzzang.jeongsan.sendmessage.navigation

import android.content.Context
import android.content.Intent
import com.kappzzang.jeongsan.intentcontract.SendMessageContract
import com.kappzzang.jeongsan.navigation.SendMessageNavigator
import com.kappzzang.jeongsan.sendmessage.SendCompleteActivity
import com.kappzzang.jeongsan.sendmessage.SendMessageActivity
import javax.inject.Inject

class SendMessageNavigatorImpl @Inject constructor() : SendMessageNavigator {
    override fun navigateToSendMessage(packageContext: Context, groupId: String): Intent {
        val intent = Intent(packageContext, SendMessageActivity::class.java)
        intent.putExtra(
            SendMessageContract.GROUP_ID,
            groupId
        )
        return intent
    }

    override fun navigateToSendComplete(packageContext: Context, groupId: String): Intent {
        val intent = Intent(packageContext, SendCompleteActivity::class.java)
        intent.putExtra(
            SendMessageContract.GROUP_ID,
            groupId
        )
        return intent
    }
}
