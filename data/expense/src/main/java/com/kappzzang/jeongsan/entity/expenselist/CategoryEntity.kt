package com.kappzzang.jeongsan.entity.expenselist

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryEntity(
    @SerializedName("name")
    val name: String,
    @SerializedName("color")
    val color: String
)
