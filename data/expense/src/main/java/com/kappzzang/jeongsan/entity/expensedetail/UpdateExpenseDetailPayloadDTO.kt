package com.kappzzang.jeongsan.entity.expensedetail

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateExpenseDetailPayloadDTO(
    @SerializedName("items")
    val items: List<ExpenseDetailSelectionInfoEntity>
)
