package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseWithExpenseIdDTO(
    @SerializedName("expense_id")
    val expenseId: String
)
