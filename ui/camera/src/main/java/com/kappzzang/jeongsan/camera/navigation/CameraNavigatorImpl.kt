package com.kappzzang.jeongsan.camera.navigation

import android.content.Context
import android.content.Intent
import com.kappzzang.jeongsan.camera.ReceiptCameraActivity
import com.kappzzang.jeongsan.navigation.CameraNavigator

class CameraNavigatorImpl : CameraNavigator {
    override fun navigateToCamera(packageContext: Context): Intent =
        Intent(packageContext, ReceiptCameraActivity::class.java)
}
