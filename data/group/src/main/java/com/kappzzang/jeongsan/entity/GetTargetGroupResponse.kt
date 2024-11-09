package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName

data class GetTargetGroupResponse(
    @SerializedName("data")
    val groupInfo: GroupInfo)
