package com.kappzzang.jeongsan.data.repositoryimpl

import com.kappzzang.jeongsan.data.datasource.MemberDatabase
import com.kappzzang.jeongsan.data.entity.MemberEntity
import com.kappzzang.jeongsan.data.entity.toVO
import com.kappzzang.jeongsan.domain.model.MemberItem
import com.kappzzang.jeongsan.domain.repository.MemberRepository
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
