package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName

data class MemberServiceIdResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<MemberServiceIdInfo>
)
