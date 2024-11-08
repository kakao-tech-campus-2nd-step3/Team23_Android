package com.kappzzang.jeongsan.main.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.kappzzang.jeongsan.main.MainActivity
import com.kappzzang.jeongsan.navigation.MainPageNavigator
import javax.inject.Inject

class MainPageNavigatorImpl @Inject constructor() : MainPageNavigator {
    override fun navigateToMainPage(packageContext: Context): Intent =
        Intent(packageContext, MainActivity::class.java)

    override fun navigateToMainPageAndEnterGroup(
        packageContext: Context,
        inviteGroup: Uri
    ): Intent {
        val intent = Intent(packageContext, MainActivity::class.java)
        intent.data = inviteGroup
        return intent
    }
}
