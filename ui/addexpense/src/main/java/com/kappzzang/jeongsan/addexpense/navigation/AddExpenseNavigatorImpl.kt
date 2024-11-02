package com.kappzzang.jeongsan.addexpense.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.kappzzang.jeongsan.addexpense.AddExpenseActivity
import com.kappzzang.jeongsan.intentcontract.AddExpenseContract
import com.kappzzang.jeongsan.model.OcrResultResponse
import com.kappzzang.jeongsan.navigation.AddExpenseNavigator

class AddExpenseNavigatorImpl : AddExpenseNavigator {
    override fun navigateToAddExpenseWithImage(
        packageContext: Context,
        ocrResponse: OcrResultResponse.OcrSuccess,
        image: Uri
    ): Intent {
        val intent = Intent(packageContext, AddExpenseActivity::class.java)
        intent.putExtra(
            AddExpenseContract.INTENT_EXPENSE_MODE, AddExpenseContract.EXPENSE_MODE_RECEIPT
        )
        intent.putExtra(
            AddExpenseContract.EXPENSE_IMAGE, image
        )
        intent.putExtra(
            AddExpenseContract.EXPENSE_DATA, ocrResponse
        )

        return intent
    }

    override fun navigateToAddExpenseManually(packageContext: Context): Intent {
        val intent = Intent(packageContext, AddExpenseActivity::class.java)
        intent.putExtra(
            AddExpenseContract.INTENT_EXPENSE_MODE, AddExpenseContract.EXPENSE_MODE_MANUAL
        )

        return intent
    }
}
