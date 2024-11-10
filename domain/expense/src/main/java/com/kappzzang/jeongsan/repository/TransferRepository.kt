package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.model.ExpenseItem
import com.kappzzang.jeongsan.model.TransferDetailItem

interface TransferRepository {
    suspend fun getTransferInfo(
        groupId: String,
        expenseIdList: List<String>
    ): Result<List<TransferDetailItem>>

    suspend fun getTransferLink(memberUuid: String): String?
    suspend fun sendTransferMessage(
        transferInfoList: List<TransferDetailItem>,
        transferLink: String,
        payeeName: String
    ): Boolean

    suspend fun getPurchasedExpenseList(groupId: String): Result<List<ExpenseItem>>
}
