package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val email: String
)
