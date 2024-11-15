package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.model.ExpenseItem
import com.kappzzang.jeongsan.model.TransferDetailItem
import com.kappzzang.jeongsan.model.TransferMessage

interface TransferRepository {
    suspend fun getTransferInfo(
        groupId: String,
        expenseIdList: List<String>
    ): Result<List<TransferDetailItem>>

    suspend fun getTransferLink(memberUuid: String): String?
    suspend fun sendTransferMessage(
        messageList: List<TransferMessage>,
        transferLink: String,
        payeeName: String
    ): Result<Unit>

    suspend fun getPurchasedExpenseList(groupId: String): Result<List<ExpenseItem>>
}
