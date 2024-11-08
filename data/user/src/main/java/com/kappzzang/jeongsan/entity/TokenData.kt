package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName

data class TokenData(
    @SerializedName("tokenType")
    val tokenType: String,
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)
