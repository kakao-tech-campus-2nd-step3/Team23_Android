package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.datasource.member.MemberDatabase
import com.kappzzang.jeongsan.datasource.remote.GroupRemoteDataSource
import com.kappzzang.jeongsan.mapper.GroupEntityMapper.toGroupItem
import com.kappzzang.jeongsan.mapper.MemberEntityMapper
import com.kappzzang.jeongsan.mapper.MemberEntityMapper.toMemberItem
import com.kappzzang.jeongsan.model.MemberItem
import com.kappzzang.jeongsan.repository.MemberRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MemberRepositoryImpl @Inject constructor(private val groupRemoteDataSource: GroupRemoteDataSource) :
    MemberRepository {
    override suspend fun addMember(groupId:String, memberId: String) {
        withContext(Dispatchers.IO) {
            val token = ""
            groupRemoteDataSource.joinGroup(token, groupId.toLong(), memberId.toLong())
        }
    }

    override suspend fun getAllMember(groupId: String): List<MemberItem> = withContext(Dispatchers.IO) {
        val token = ""
        groupRemoteDataSource.getMemberInfo(token, groupId.toLong()).fold(
            onSuccess = { memberInfoList ->
                memberInfoList.map { it.toMemberItem() }
            },
            onFailure = {
                listOf()
            }
        )
    }
}
