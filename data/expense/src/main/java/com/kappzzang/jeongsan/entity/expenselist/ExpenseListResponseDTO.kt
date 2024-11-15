package com.kappzzang.jeongsan.entity.expenselist

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseListResponseDTO(
    @SerializedName("expenseList")
    val expenseList: List<ExpenseRemoteEntity>,
    @SerializedName("totalPrice")
    val totalPrice: Long,
    @SerializedName("totalPersonalExpense")
    val myTotalExpense: Int? = null
)
