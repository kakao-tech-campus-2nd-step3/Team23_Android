package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CreateGroupRequestDTO(
    @SerializedName("name") val name: String,
    @SerializedName("subject") val subject: String,
    @SerializedName("members") val memberIdList: List<String>
)
