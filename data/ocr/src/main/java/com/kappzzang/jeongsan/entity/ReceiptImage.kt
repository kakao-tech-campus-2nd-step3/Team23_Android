package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ReceiptImage(
    @SerializedName("format")
    val format: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("data")
    val base64Encoded: String,
    @SerializedName("url")
    val url: String?
)
