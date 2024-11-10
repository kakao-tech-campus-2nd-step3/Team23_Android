package com.kappzzang.jeongsan.repositoryimpl

import android.util.Log
import com.kakao.sdk.talk.TalkApiClient
import com.kappzzang.jeongsan.repository.ExpenseMessageRepository
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ExpenseMessageRepositoryImpl @Inject constructor() : ExpenseMessageRepository {

    override suspend fun sendNewExpenseMessage(
        expenseId: String,
        expenseName: String,
        payerName: String,
        groupId: String,
        memberUuidList: List<String>
    ): Boolean = suspendCoroutine { continuation ->
        TalkApiClient.instance.sendCustomMessage(
            receiverUuids = memberUuidList,
            templateId = INVITE_MESSAGE_TEMPLATE_ID,
            templateArgs = mapOf(
                EXPENSE_ID to expenseId,
                EXPENSE_NAME to expenseName,
                PAYER_NAME to payerName,
                GROUP_ID to groupId
            )
        ) { result, error ->
            if (error != null) {
                Log.e(TAG, "새 지출 등록 메시지 전송 실패", error)
                continuation.resume(false)
            } else if (result != null) {
                Log.i(TAG, "새 지출 등록 메시지 전송 성공")
                if (result.failureInfos != null) {
                    Log.i(TAG, "일부에게 새 지출 등록 메시지 전송 실패")
                }
                continuation.resume(true)
            }
        }
    }

    companion object {
        private const val INVITE_MESSAGE_TEMPLATE_ID = 114103L
        private const val TAG = "ExpenseMessageRepositoryImpl"
        private const val EXPENSE_ID = "EXPENSE_ID"
        private const val EXPENSE_NAME = "EXPENSE_TITLE"
        private const val PAYER_NAME = "PAYER"
        private const val GROUP_ID = "GROUP_ID"
    }
}
