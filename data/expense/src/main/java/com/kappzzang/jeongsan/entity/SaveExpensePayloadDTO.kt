package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SaveExpensePayloadDTO(
    @SerializedName("title")
    val title: String,
    @SerializedName("paymentTime")
    val paymentTime: String,
    @SerializedName("categoryId")
    val categoryId: Long,
    @SerializedName("image")
    val image: ImageEntity,
    @SerializedName("items")
    val items: List<ExpenseItemEntity>
)
