package com.kappzzang.jeongsan.navigation

import android.content.Context
import android.content.Intent

interface CreateGroupNavigator {
    fun navigateToCreateGroup(packageContext: Context): Intent
}
