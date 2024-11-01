package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName

data class RefreshTokenData(
    @SerializedName("tokenType")
    val tokenType: String,
    @SerializedName("accessToken")
    val accessToken: String
)