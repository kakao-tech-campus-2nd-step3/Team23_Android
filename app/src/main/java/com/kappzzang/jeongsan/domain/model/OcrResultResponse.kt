package com.kappzzang.jeongsan.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
sealed class OcrResultResponse : Parcelable {
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