package com.kappzzang.jeongsan.domain.model

import java.util.Date

sealed class OcrResultResponse {
    data class OcrSuccess(
        val name: String,
        val paymentTime: Date,
        val detailItems: List<ExpenseDetailItem>
    ) : OcrResultResponse()

    data class OcrFailed(
        val message: String,
        val code: Int
    ) : OcrResultResponse()
}
