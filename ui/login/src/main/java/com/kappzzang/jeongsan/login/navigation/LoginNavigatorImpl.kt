package com.kappzzang.jeongsan.login.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.kappzzang.jeongsan.login.LoginActivity
import com.kappzzang.jeongsan.navigation.LoginNavigator
import javax.inject.Inject

class LoginNavigatorImpl @Inject constructor(): LoginNavigator {
    override fun login(packageContext: Context): Intent =
        Intent(packageContext, LoginActivity::class.java)

    override fun loginAndEnterGroup(packageContext: Context, inviteGroup: Uri): Intent {
        val intent = Intent(packageContext, LoginActivity::class.java)
        intent.data = inviteGroup
        return intent
    }

}
