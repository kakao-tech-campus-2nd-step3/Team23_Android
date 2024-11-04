package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName

data class MemberPreview(
    @SerializedName("name")
    val name: String,
    @SerializedName("profileImage")
    val profileImageUrl: String
)
