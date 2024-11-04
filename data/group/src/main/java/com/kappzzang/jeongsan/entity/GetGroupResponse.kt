package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName

data class GetGroupResponse(
    @SerializedName("teamsWithProfiles")
    val groupList: List<GroupInfo>
)
