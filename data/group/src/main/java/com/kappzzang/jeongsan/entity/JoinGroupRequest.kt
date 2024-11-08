package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName

data class JoinGroupRequest(
    @SerializedName("memberId")
    val myId: Long
)
