package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class GetTransferListResponseDTO(
    // TODO: 아직 API내 필드 명이 명시되지 않음.
    @SerializedName("undefined")
    val transferList: List<TransferItemEntity>
)
