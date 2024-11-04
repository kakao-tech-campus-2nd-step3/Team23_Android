package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName

data class CreateGroupResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("errorCode")
    val errorCode: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: GroupId
) {
    // data 내부에 groupId밖에 없어 바로 접근 하기 위해 구현
    val groupId: Long
        get() = data.groupId
}

data class GroupId(
    @SerializedName("teamId")
    val groupId: Long
)
