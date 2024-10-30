package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseDetailItemEntity(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("unitPrice")
    val unitPrice: Int,
    @SerializedName("consumedQuantity")
    val quantityConsumed: Int
)
