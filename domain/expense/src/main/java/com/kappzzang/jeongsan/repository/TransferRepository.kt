package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.model.TransferDetailItem

interface TransferRepository {
    suspend fun getTransferInfo(): List<TransferDetailItem>
    suspend fun getTransferLink(memberUuid: String): String?
    suspend fun sendTransferMessage(
        transferInfoList: List<TransferDetailItem>,
        transferLink: String,
        payeeName: String,
    ): Boolean
}
