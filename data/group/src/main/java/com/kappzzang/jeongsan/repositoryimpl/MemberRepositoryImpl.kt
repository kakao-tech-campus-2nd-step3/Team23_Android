package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.datasource.member.MemberDatabase
import com.kappzzang.jeongsan.mapper.MemberEntityMapper
import com.kappzzang.jeongsan.model.MemberItem
import com.kappzzang.jeongsan.repository.MemberRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MemberRepositoryImpl @Inject constructor(private val memberDatabase: MemberDatabase) :
    MemberRepository {
    override suspend fun addMember(member: MemberItem) {
        withContext(Dispatchers.IO) {
            memberDatabase.getMemberDao().addMember(
                MemberEntityMapper.mapMemberToMemberEntity(member)
            )
        }
    }

    override suspend fun getAllMember(): List<MemberItem> = withContext(Dispatchers.IO) {
        memberDatabase.getMemberDao().getAllMember().map {
            MemberEntityMapper.mapMemberEntityToMember(it)
        }
    }
}
