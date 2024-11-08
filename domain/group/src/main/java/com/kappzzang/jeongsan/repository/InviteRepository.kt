package com.kappzzang.jeongsan.repository

interface InviteRepository {
    suspend fun sendInviteMessage(
        groupId: String,
        groupName: String,
        memberUuidList: List<String>
    ): Boolean
}
