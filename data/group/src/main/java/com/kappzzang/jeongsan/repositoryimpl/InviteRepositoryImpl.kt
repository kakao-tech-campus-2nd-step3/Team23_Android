package com.kappzzang.jeongsan.repositoryimpl

import android.util.Log
import com.kakao.sdk.talk.TalkApiClient
import com.kappzzang.jeongsan.repository.InviteRepository
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class InviteRepositoryImpl @Inject constructor() : InviteRepository {

    // TODO: 추후 친구에게 보내기로 수정
    override suspend fun sendInviteMessage(
        groupId: String,
        groupName: String,
        memberId: String
    ): Boolean = suspendCoroutine { continuation ->
        TalkApiClient.instance.sendCustomMemo(
            templateId = INVITE_MESSAGE_TEMPLATE_ID,
            templateArgs = mapOf(
                GROUP_ID to groupId,
                GROUP_NAME to groupName
            )
        ) { error ->
            if (error != null) {
                Log.e(TAG, "초대 메시지 전송 실패", error)
                continuation.resume(false)
            } else {
                Log.i(TAG, "초대 메시지 전송 성공")
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
