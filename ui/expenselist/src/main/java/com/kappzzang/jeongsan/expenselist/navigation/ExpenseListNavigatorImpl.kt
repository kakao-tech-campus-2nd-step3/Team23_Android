package com.kappzzang.jeongsan.expenselist.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.kappzzang.jeongsan.expenselist.ExpenseListActivity
import com.kappzzang.jeongsan.intentcontract.ExpenseListContract
import com.kappzzang.jeongsan.intentcontract.ReceiptCameraContract
import com.kappzzang.jeongsan.model.OcrResultResponse
import com.kappzzang.jeongsan.navigation.ExpenseListNavigator
import javax.inject.Inject

class ExpenseListNavigatorImpl @Inject constructor(): ExpenseListNavigator {
    override fun navigateToExpenseList(packageContext: Context, groupId: String): Intent {
        val intent = Intent(packageContext, ExpenseListActivity::class.java)
        intent.putExtra(ExpenseListContract.GROUP_ID, groupId)

        return intent
    }

    override fun getExpenseListCancelResult(packageContext: Context): Intent =
        Intent(packageContext, ExpenseListActivity::class.java)

    override fun getExpenseListWithOcrDataResult(
        packageContext: Context,
        result: OcrResultResponse,
        image: Uri
    ): Intent {
        val intent = Intent(packageContext, ExpenseListActivity::class.java)

        intent.putExtra(ReceiptCameraContract.OCR_RESULT, result)
        intent.putExtra(ReceiptCameraContract.OCR_RESULT_IMAGE, image)

        return intent
    }
}
