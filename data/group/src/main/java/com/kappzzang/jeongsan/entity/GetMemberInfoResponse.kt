package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName

data class GetMemberInfoResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("errorCode")
    val errorCode: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val memberList: List<MemberInfo>
)
