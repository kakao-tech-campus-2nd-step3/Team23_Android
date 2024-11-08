package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName

data class OcrResultDetailItem(
    @SerializedName("name")
    val name: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("unitPrice")
    val unitPrice: Int
)
