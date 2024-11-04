package com.kappzzang.jeongsan.entity.expensedetail

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseDetailSelectionInfoEntity(
    @SerializedName("itemId")
    val itemId: Int,
    @SerializedName("quantity")
    val quantity: Int
)
