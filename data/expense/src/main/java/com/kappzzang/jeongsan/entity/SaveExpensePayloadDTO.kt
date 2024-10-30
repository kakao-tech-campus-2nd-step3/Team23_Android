package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SaveExpensePayloadDTO(
    @SerializedName("title")
    val title:String,
    @SerializedName("payment_time")
    val paymentTime:String,
    @SerializedName("category_id")
    val categoryId: Long,
    @SerializedName("image")
    val image: ImageDTO,
    @SerializedName("items")
    val items: List<ExpenseItemDTO>
)
