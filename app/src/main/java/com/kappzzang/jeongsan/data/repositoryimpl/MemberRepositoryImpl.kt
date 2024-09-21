package com.kappzzang.jeongsan.data.repositoryimpl

import com.kappzzang.jeongsan.domain.model.MemberItem
import com.kappzzang.jeongsan.domain.repository.MemberRepository
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor() : MemberRepository {
    override suspend fun getAllMember(): List<MemberItem> {
        return listOf()
    }
}