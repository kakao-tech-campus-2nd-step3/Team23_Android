package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.model.MemberItem

interface MemberRepository {
    suspend fun getAllMember(groupId: String): List<MemberItem>
}
