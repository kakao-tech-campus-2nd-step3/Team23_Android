package com.kappzzang.jeongsan.repositoryimpl

import android.util.Log
import com.kakao.sdk.talk.TalkApiClient
import com.kappzzang.jeongsan.repository.InviteRepository
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class InviteRepositoryImpl @Inject constructor() : InviteRepository {

    override suspend fun sendInviteMessage(
        groupId: String,
        groupName: String,
        memberUuidList: List<String>
    ): Boolean = suspendCoroutine { continuation ->
        TalkApiClient.instance.sendCustomMessage(
            receiverUuids = memberUuidList,
            templateId = INVITE_MESSAGE_TEMPLATE_ID,
            templateArgs = mapOf(
                GROUP_ID to groupId,
                GROUP_NAME to groupName
            )
        ) { result, error ->
            if (error != null) {
                Log.e(TAG, "초대 메시지 전송 실패", error)
                continuation.resume(false)
            } else if (result != null) {
                Log.i(TAG, "초대 메시지 전송 성공")
                if (result.failureInfos != null) {
                    Log.i(TAG, "일부에게 초대 메시지 전송 실패")
                }
                continuation.resume(true)
            }
        }
    }

    companion object {
        private const val INVITE_MESSAGE_TEMPLATE_ID = 113229L
        private const val GROUP_ID = "group_id"
        private const val GROUP_NAME = "group_name"
        private const val TAG = "InviteRepositoryImpl"
    }
}
