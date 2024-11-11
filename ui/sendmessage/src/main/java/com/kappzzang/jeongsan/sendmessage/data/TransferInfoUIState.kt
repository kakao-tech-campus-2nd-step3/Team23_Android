package com.kappzzang.jeongsan.sendmessage.data

import com.kappzzang.jeongsan.model.TransferDetailItem

interface HasExpenseId {
    val expenseIdList: List<String>
}

interface HasTransferInfo {
    val transferInfoList: List<TransferDetailItem>
    val totalExpenseToGet: Int
}

sealed class TransferInfoUIState {
    data class PurchaseListGetSuccess(
        val size: Int,
        val totalPay: Int,
        override val expenseIdList: List<String>
    ) : TransferInfoUIState(),
        HasExpenseId

    data class LoadingTransferInfo(override val expenseIdList: List<String>) :
        TransferInfoUIState(),
        HasExpenseId

    data class TransferInfoGetSuccess(
        override val transferInfoList: List<TransferDetailItem>,
        override val totalExpenseToGet: Int,
        override val expenseIdList: List<String>
    ) : TransferInfoUIState(),
        HasTransferInfo,
        HasExpenseId

    data class TransferInfoGetError(val message: String) : TransferInfoUIState()

    data class SendingTransferMessage(
        override val transferInfoList: List<TransferDetailItem>,
        override val totalExpenseToGet: Int,
        override val expenseIdList: List<String>
    ) : TransferInfoUIState(),
        HasTransferInfo,
        HasExpenseId

    data class TransferMessageSendSuccess(
        override val transferInfoList: List<TransferDetailItem>,
        override val totalExpenseToGet: Int,
        override val expenseIdList: List<String>
    ) : TransferInfoUIState(),
        HasTransferInfo,
        HasExpenseId

    data class UpdatingExpenseState(
        override val transferInfoList: List<TransferDetailItem>,
        override val totalExpenseToGet: Int,
        override val expenseIdList: List<String>
    ) : TransferInfoUIState(),
        HasTransferInfo,
        HasExpenseId

    data object ExpenseStateUpdateSuccess : TransferInfoUIState()
    data class ExpenseStateUpdateError(val message: String) : TransferInfoUIState()

    data object Idle : TransferInfoUIState()
    data object LoadingPurchaseList : TransferInfoUIState()

    data class PurchaseListGetError(val message: String) : TransferInfoUIState()

    data class TransferMessageSendError(val message: String) : TransferInfoUIState()
}
