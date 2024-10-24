package com.kappzzang.jeongsan.model

import android.os.Parcelable
import java.util.Date
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class OcrResultResponse : Parcelable {
    data class OcrSuccess(
        val name: String,
        val paymentTime: Date,
        val detailItems: List<OcrDetailItem>
    ) : OcrResultResponse()

    data class OcrFailed(val message: String, val code: Int) : OcrResultResponse()
}
