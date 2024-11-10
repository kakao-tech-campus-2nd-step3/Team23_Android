package com.kappzzang.jeongsan.addexpense.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.kappzzang.jeongsan.addexpense.AddExpenseActivity
import com.kappzzang.jeongsan.intentcontract.AddExpenseContract
import com.kappzzang.jeongsan.model.OcrResultResponse
import com.kappzzang.jeongsan.navigation.AddExpenseNavigator
import javax.inject.Inject

class AddExpenseNavigatorImpl @Inject constructor() : AddExpenseNavigator {
    override fun navigateToAddExpenseWithImage(
        packageContext: Context,
        ocrResponse: OcrResultResponse.OcrSuccess,
        image: Uri,
        groupId: String
    ): Intent {
        val intent = Intent(packageContext, AddExpenseActivity::class.java)
        intent.putExtra(
            AddExpenseContract.INTENT_EXPENSE_MODE,
            AddExpenseContract.EXPENSE_MODE_RECEIPT
        )
        intent.putExtra(
            AddExpenseContract.EXPENSE_IMAGE,
            image
        )
        intent.putExtra(
            AddExpenseContract.EXPENSE_DATA,
            ocrResponse
        )
        intent.putExtra(
            AddExpenseContract.GROUP_ID,
            groupId
        )

        return intent
    }

    override fun navigateToAddExpenseManually(packageContext: Context, groupId: String): Intent {
        val intent = Intent(packageContext, AddExpenseActivity::class.java)
        intent.putExtra(
            AddExpenseContract.INTENT_EXPENSE_MODE,
            AddExpenseContract.EXPENSE_MODE_MANUAL
        )
        intent.putExtra(
            AddExpenseContract.GROUP_ID,
            groupId
        )

        return intent
    }
}
