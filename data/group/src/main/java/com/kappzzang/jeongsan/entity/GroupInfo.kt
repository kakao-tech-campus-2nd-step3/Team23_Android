package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName

data class GroupInfo(
    @SerializedName("teamId")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("ownerKakaoId")
    val ownerServiceId: Int?,
    @SerializedName("isCompleted")
    val isCompleted: Boolean,
    @SerializedName("subject")
    val subject: String,
    @SerializedName("memberPreviews")
    val previewList: List<MemberPreview>
)
