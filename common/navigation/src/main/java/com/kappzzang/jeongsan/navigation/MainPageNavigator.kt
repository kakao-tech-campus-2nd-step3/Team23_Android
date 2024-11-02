package com.kappzzang.jeongsan.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri

interface MainPageNavigator {
    fun navigateToMainPage(packageContext: Context): Intent

    fun navigateToMainPageAndEnterGroup(packageContext: Context, inviteGroup: Uri): Intent
}
