package com.kappzzang.jeongsan.repository

interface MemberRepository {
    suspend fun addMember(member: com.kappzzang.jeongsan.model.MemberItem)
    suspend fun getAllMember(): List<com.kappzzang.jeongsan.model.MemberItem>
}
