package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateExpenseStatePayloadDTO(
    @SerializedName("state")
    val state: String,
    @SerializedName("expenses")
    val expenses: List<SimpleExpenseItemEntity>
)
