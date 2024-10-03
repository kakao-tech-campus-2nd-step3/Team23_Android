package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.datasource.member.MemberDatabase
import com.kappzzang.jeongsan.entity.MemberEntity
import com.kappzzang.jeongsan.entity.toVO
import com.kappzzang.jeongsan.model.MemberItem
import com.kappzzang.jeongsan.repository.MemberRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MemberRepositoryImpl @Inject constructor(private val memberDatabase: MemberDatabase) :
    MemberRepository {
    override suspend fun addMember(member: MemberItem) {
        withContext(Dispatchers.IO) {
            memberDatabase.getMemberDao().addMember(member.toEntity())
        }
    }

    override suspend fun getAllMember(): List<MemberItem> = withContext(Dispatchers.IO) {
        memberDatabase.getMemberDao().getAllMember().map { it.toVO() }
    }

    private fun MemberItem.toEntity() = MemberEntity(
        id,
        name,
        profileImageURL,
        isInvited
    )
}
