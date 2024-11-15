package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class GetTransferListPayloadDTO(
    @SerializedName("expenses")
    val expenseList: List<SimpleExpenseItemEntity>
)
