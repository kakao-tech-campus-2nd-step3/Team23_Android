package com.kappzzang.jeongsan.util

import android.content.Intent
import android.os.Build
import com.kappzzang.jeongsan.domain.model.OcrResultResponse
import com.kappzzang.jeongsan.ui.camera.ReceiptCameraActivity

object IntentHelper {
    inline fun <reified T> Intent.getParcelableData(key: String): T?{
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelableExtra(
                key,
                T::class.java
            )
        } else {
            getParcelableExtra(key)
        }
        return data
    }
}
