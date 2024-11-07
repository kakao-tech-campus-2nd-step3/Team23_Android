package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.model.MemberItem

interface MemberRepository {
    suspend fun addMember(groupId: String, memberId: String)
    suspend fun getAllMember(groupId: String): List<MemberItem>
}
