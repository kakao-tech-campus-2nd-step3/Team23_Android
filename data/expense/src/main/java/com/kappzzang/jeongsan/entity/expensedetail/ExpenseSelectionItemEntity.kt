package com.kappzzang.jeongsan.entity.expensedetail

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseSelectionItemEntity(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("unitPrice")
    val unitPrice: Int,
    @SerializedName("personalExpense")
    val selectorList: List<ExpenseSelectorEntity>
)
