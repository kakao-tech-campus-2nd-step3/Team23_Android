package com.kappzzang.jeongsan.entity.expensedetail

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseSelectorEntity(
    @SerializedName("nickname")
    val name: String,
    @SerializedName("profileImage")
    val profileImage: String,
    @SerializedName("quantity")
    val selectedQuantity: Int
)
