package com.kappzzang.jeongsan.model

data class TransferDetailItem(
    val memberId: String,
    val serviceId: String,
    val name: String,
    val fee: Int,
    val profileImageUrl: String
)

data class TransferMessage(val uuid: String, val fee: Int)
