package com.kappzzang.jeongsan.addexpense.data

sealed class ExpenseUploadUIState {
    data object Idle : ExpenseUploadUIState()
    data object UploadFailed : ExpenseUploadUIState()
    data object Uploading : ExpenseUploadUIState()

    data class UploadSuccess(val expenseId: String) : ExpenseUploadUIState()
    data class UploadSuccessAndSendSuccess(val expenseId: String) : ExpenseUploadUIState()
    data class UploadSuccessAndSendFailed(val expenseId: String) : ExpenseUploadUIState()
}
