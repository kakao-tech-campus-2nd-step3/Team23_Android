package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("profileImage")
    val profileImageUrl: String
)