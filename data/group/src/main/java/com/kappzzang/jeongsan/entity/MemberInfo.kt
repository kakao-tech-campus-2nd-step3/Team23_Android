package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName

data class MemberInfo(
    @SerializedName("memberId")
    val id: Int,
    @SerializedName("nickname")
    val name: String,
    @SerializedName("profileImage")
    val profileImageUrl: String,
    @SerializedName("isInviteAccepted")
    val isInvited: Boolean
)
