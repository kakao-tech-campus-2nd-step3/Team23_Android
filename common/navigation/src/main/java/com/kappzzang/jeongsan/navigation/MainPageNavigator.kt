package com.kappzzang.jeongsan.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri

interface MainPageNavigator {
    fun navigateToMainPage(packageContext: Context): Intent

    fun navigateToMainPageAndWithUri(packageContext: Context, infoUri: Uri): Intent
}
