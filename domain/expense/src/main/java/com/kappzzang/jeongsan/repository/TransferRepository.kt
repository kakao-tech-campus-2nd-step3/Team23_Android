package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.model.TransferDetailItem

interface TransferRepository {
    suspend fun getTransferInfo(): List<TransferDetailItem>
}
