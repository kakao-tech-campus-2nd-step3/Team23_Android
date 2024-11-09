package com.kappzzang.jeongsan.repository

interface InviteRepository {
    suspend fun sendInviteMessage(
        groupId: String,
        groupName: String,
        memberServiceIdList: List<String>
    ): Boolean
}
