package com.kappzzang.jeongsan.navigation

import android.content.Context
import android.content.Intent

interface CameraNavigator {
    fun navigateToCamera(packageContext: Context): Intent
}
