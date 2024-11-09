package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    // TODO: 현재 API 상으로는 여전히 필드명이 uuid이라 수정하지 않았습니다.
    @SerializedName("uuid")
    val serviceId: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("profileImage")
    val profileImageUrl: String
)
