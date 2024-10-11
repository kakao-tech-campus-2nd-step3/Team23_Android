package com.kappzzang.jeongsan.repositoryimpl

import android.util.Log
import com.kakao.sdk.talk.TalkApiClient
import com.kappzzang.jeongsan.model.TransferDetailItem
import com.kappzzang.jeongsan.repository.TransferRepository
import com.kappzzang.jeongsan.util.IntegerFormatter.formatDecimalSeparator
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TransferRepositoryImpl @Inject constructor() : TransferRepository {
    override suspend fun getTransferInfo(): List<TransferDetailItem> {
        // TODO: 일단 임시 데이터 반환
        return listOf(
            TransferDetailItem(
                "1",
                "라이언",
                5000,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRcE9NGEQuAXw1Cg98jbhhhUfM2IpZcWilwHg&s"
            ),
            TransferDetailItem(
                "2",
                "춘식이",
                9000,
                "https://i.namu.wiki/i/GQMqb8jtiqpCo6_US7jmWDO30KfPB2MMvbdURVub61Rs6ALKqbG-nUATj-wNk7bXXWIDjiLHJxWYkTELUgybkA.webp"
            ),
            TransferDetailItem(
                "3",
                "네오",
                4000,
                "https://t4.daumcdn.net/thumb/R720x0.fjpg/?fname=http://t1.daumcdn.net/brunch/service/user/cnoC/image/r5RjC7tUqK6Mb4NVX0f5d-qrCSQ.jpg"
            )
        )
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

    companion object {
        private const val TRANSFER_MESSAGE_TEMPLATE_ID = 112933L
        private const val TAG = "TransferRepositoryImpl"
    }
}
