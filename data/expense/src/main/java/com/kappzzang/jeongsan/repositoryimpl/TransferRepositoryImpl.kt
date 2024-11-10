package com.kappzzang.jeongsan.repositoryimpl

import android.util.Log
import com.kakao.sdk.talk.TalkApiClient
import com.kappzzang.jeongsan.datasource.ExpenseListRemoteDatasource
import com.kappzzang.jeongsan.mapper.ExpenseEntityMapper
import com.kappzzang.jeongsan.mapper.ExpenseListEntityMapper
import com.kappzzang.jeongsan.model.ExpenseItem
import com.kappzzang.jeongsan.model.TransferDetailItem
import com.kappzzang.jeongsan.model.TransferMessage
import com.kappzzang.jeongsan.repository.TransferRepository
import com.kappzzang.jeongsan.util.IntegerFormatter.formatDecimalSeparator
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TransferRepositoryImpl @Inject constructor(
    private val expenseListRemoteDatasource: ExpenseListRemoteDatasource,
    private val kakaoClient: TalkApiClient
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
            it.map { transferItemEntity ->
                ExpenseEntityMapper.mapTransferEntityToModel(transferItemEntity)
            }
        }
    }

    override suspend fun getTransferLink(memberUuid: String): String? {
        // TODO: 일단 임시 송금 링크 반환
        return "https://link.kakaopay.com/_/f1Rxe4x"
    }

    override suspend fun sendTransferMessage(
        messageList: List<TransferMessage>,
        transferLink: String,
        payeeName: String
    ): Result<Unit> = suspendCoroutine { continuation ->
        messageList.forEach {
            kakaoClient.sendCustomMessage(
                receiverUuids = listOf(it.uuid),
                templateId = TRANSFER_MESSAGE_TEMPLATE_ID,
                templateArgs = mapOf(
                    "price" to it.fee.formatDecimalSeparator() + "원",
                    "payee" to payeeName,
                    "link" to transferLink
                )
            ) { result, error ->
                if (error != null) {
                    Log.e(TAG, "새 지출 등록 메시지 전송 실패", error)
                    continuation.resume(Result.failure(error))
                } else if (result != null) {
                    Log.i(TAG, "새 지출 등록 메시지 전송 성공")
                    if (result.failureInfos != null) {
                        Log.i(TAG, "일부에게 새 지출 등록 메시지 전송 실패")
                    }
                    continuation.resume(Result.success(Unit))
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
