package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName

data class MemberInfo(
    @SerializedName("kakaoId")
    val id: String,
    @SerializedName("nickname")
    val name: String,
    @SerializedName("profileImage")
    val profileImageUrl: String,
    @SerializedName("isInviteAccepted")
    val isInvited: Boolean
)
