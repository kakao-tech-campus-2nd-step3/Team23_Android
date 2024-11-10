package com.kappzzang.jeongsan.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.kappzzang.jeongsan.model.OcrResultResponse

interface ExpenseListNavigator {
    fun navigateToExpenseList(packageContext: Context, groupId: String): Intent

    fun navigateToExpenseListWithNewExpense(
        packageContext: Context,
        groupId: String,
        expenseId: String
    ): Intent

    fun getExpenseListCancelResult(packageContext: Context): Intent

    fun getExpenseListWithOcrDataResult(
        packageContext: Context,
        result: OcrResultResponse,
        image: Uri
    ): Intent
}
