package com.kappzzang.jeongsan.domain.repository

import com.kappzzang.jeongsan.domain.model.MemberItem

interface MemberRepository {
    suspend fun getAllMember(): List<MemberItem>
}