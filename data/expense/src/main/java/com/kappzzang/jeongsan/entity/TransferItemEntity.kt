package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class TransferItemEntity(
    @SerializedName("kakaoId")
    val serviceId: String,
    @SerializedName("memberId")
    val memberId: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("amountDue")
    val expenseToTransfer: Int,
    @SerializedName("profileImage")
    val profileImageUrl: String
)
