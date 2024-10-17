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
        idList: List<String>,
        inviteLink: String,
        groupName: String
    ): Boolean {
        return suspendCoroutine { continuation ->
            TalkApiClient.instance.sendCustomMemo(
                templateId = INVITE_MESSAGE_TEMPLATE_ID,
                templateArgs = mapOf(
                    "link" to inviteLink,
                    "group_name" to groupName
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
    }

    override fun getInviteLink(): String {
        // TODO: 임시 링크 반환
        return "https://www.google.com"
    }

    companion object {
        private const val INVITE_MESSAGE_TEMPLATE_ID = 113229L
        private const val TAG = "InviteRepositoryImpl"
    }
}