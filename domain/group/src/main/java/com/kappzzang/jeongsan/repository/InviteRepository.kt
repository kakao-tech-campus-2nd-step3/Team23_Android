package com.kappzzang.jeongsan.repository

interface InviteRepository {
    suspend fun sendInviteMessage(
        idList: List<String>,
        inviteLink: String,
        groupName: String
    ): Boolean

    fun getInviteLink(): String
}