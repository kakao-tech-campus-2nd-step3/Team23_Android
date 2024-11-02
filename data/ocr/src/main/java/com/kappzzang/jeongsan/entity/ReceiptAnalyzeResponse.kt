package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName

data class ReceiptAnalyzeResponse(
    @SerializedName("title")
    val title: String,
    @SerializedName("paymentTime")
    val paymentTime: String,
    @SerializedName("items")
    val items: List<OcrResultDetailItem>
)
