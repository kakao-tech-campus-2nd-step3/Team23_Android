package com.kappzzang.jeongsan.entity.expensedetail

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseDetailEntity(
    @SerializedName("title")
    val title:String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("items")
    val detailItems: List<ExpenseDetailItemEntity>
)
