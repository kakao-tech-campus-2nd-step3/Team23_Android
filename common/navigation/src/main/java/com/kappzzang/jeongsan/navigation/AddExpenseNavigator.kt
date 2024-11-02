package com.kappzzang.jeongsan.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.kappzzang.jeongsan.model.OcrResultResponse

interface AddExpenseNavigator {
    fun navigateToAddExpenseWithImage(
        packageContext: Context,
        ocrResponse: OcrResultResponse.OcrSuccess,
        image: Uri
    ): Intent

    fun navigateToAddExpenseManually(packageContext: Context): Intent
}
