package com.kappzzang.jeongsan.repositoryimpl

import android.util.Log
import com.kakao.sdk.talk.TalkApiClient
import com.kappzzang.jeongsan.datasource.ExpenseListRemoteDatasource
import com.kappzzang.jeongsan.mapper.ExpenseEntityMapper
import com.kappzzang.jeongsan.mapper.ExpenseListEntityMapper
import com.kappzzang.jeongsan.model.ExpenseItem
import com.kappzzang.jeongsan.model.TransferDetailItem
import com.kappzzang.jeongsan.repository.TransferRepository
import com.kappzzang.jeongsan.util.IntegerFormatter.formatDecimalSeparator
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TransferRepositoryImpl @Inject constructor(
    private val expenseListRemoteDatasource: ExpenseListRemoteDatasource
) : TransferRepository {
    override suspend fun getTransferInfo(
        groupId: String,
        expenseIdList: List<String>
    ): Result<List<TransferDetailItem>> {
        val result = expenseListRemoteDatasource.getTransferList(
            groupId = groupId,
            expenseList = expenseIdList
        )

        return result.mapCatching {
            it.transferList.map { transferItemEntity ->
                ExpenseEntityMapper.mapTransferEntityToModel(transferItemEntity)
            }
        }
    }

    override suspend fun getTransferLink(memberUuid: String): String? {
        // TODO: 일단 임시 송금 링크 반환
        return "https://link.kakaopay.com/_/f1Rxe4x"
    }

    override suspend fun sendTransferMessage(
        transferInfoList: List<TransferDetailItem>,
        transferLink: String,
        payeeName: String
    ): Boolean {
        // TODO: 정보를 보낼 친구의 UUID를 현재 알 수 없으므로, 나에게 보내기로 확인 (첫번째 값으로 메시지)
        return suspendCoroutine { continuation ->
            TalkApiClient.instance.sendCustomMemo(
                templateId = TRANSFER_MESSAGE_TEMPLATE_ID,
                templateArgs = mapOf(
                    "price" to transferInfoList[0].fee.formatDecimalSeparator() + "원",
                    "payee" to payeeName,
                    "link" to transferLink
                )
            ) { error ->
                if (error != null) {
                    Log.e(TAG, "송금 메시지 전송 실패", error)
                    continuation.resume(false)
                } else {
                    Log.i(TAG, "송금 메시지 전송 성공")
                    continuation.resume(true)
                }
            }
        }
    }

    override suspend fun getPurchasedExpenseList(groupId: String): Result<List<ExpenseItem>> {
        val response = expenseListRemoteDatasource.getPurchasedExpenseList(groupId)
        return response.mapCatching {
            it.expenseList.map { expense ->
                ExpenseListEntityMapper.mapPurchaseExpenseListToModel(expense)
            }
        }
    }

    companion object {
        private const val TRANSFER_MESSAGE_TEMPLATE_ID = 112933L
        private const val TAG = "TransferRepositoryImpl"
    }
}
