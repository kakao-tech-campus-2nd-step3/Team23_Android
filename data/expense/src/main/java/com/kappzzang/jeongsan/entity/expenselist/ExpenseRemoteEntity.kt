package com.kappzzang.jeongsan.entity.expenselist

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseRemoteEntity(
    @SerializedName("expenseId")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("payerId")
    val payerServiceId: Long?,
    @SerializedName("totalPrice")
    val totalPrice: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("category")
    val category: CategoryEntity,
    @SerializedName("checked")
    val checked: Boolean? = null,
    @SerializedName("personalExpense")
    val myExpense: Int? = null
)
