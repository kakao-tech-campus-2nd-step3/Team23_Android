package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ImageEntity(
    @SerializedName("format")
    val format: String,
    @SerializedName("url")
    val url: String?,
    @SerializedName("data")
    val data: String,
    @SerializedName("name")
    val name: String
)
