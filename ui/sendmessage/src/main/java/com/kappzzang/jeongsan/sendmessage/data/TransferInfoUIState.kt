package com.kappzzang.jeongsan.sendmessage.data

import com.kappzzang.jeongsan.model.TransferDetailItem

sealed class TransferInfoUIState {
    data object Idle : TransferInfoUIState()
    data object LoadingPurchaseList : TransferInfoUIState()
    data class PurchaseListGetSuccess(
        val size: Int,
        val totalPay: Int,
        val expenseIdList: List<String>
    ) : TransferInfoUIState()

    data class PurchaseListGetError(
        val message: String
    ) : TransferInfoUIState()

    data object LoadingTransferInfo : TransferInfoUIState()
    data class TransferInfoGetSuccess(
        val transferInfoList: List<TransferDetailItem>,
        val totalExpenseToGet: Int,
    ) : TransferInfoUIState()

    data class TransferInfoGetError(
        val message: String
    ) : TransferInfoUIState()

    data object SendingTransferMessage : TransferInfoUIState()

    data object TransferMessageSendSuccess : TransferInfoUIState()

    data class TransferMessageSendError(
        val message: String
    ) : TransferInfoUIState()
}
