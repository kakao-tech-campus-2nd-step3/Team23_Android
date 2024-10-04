package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.model.MemberItem

interface MemberRepository {
    suspend fun addMember(member: MemberItem)
    suspend fun getAllMember(): List<MemberItem>
}
