package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.datasource.GroupRemoteDataSource
import com.kappzzang.jeongsan.mapper.GroupEntityMapper.toGroupItem
import com.kappzzang.jeongsan.mapper.GroupEntityMapper.toServiceIdList
import com.kappzzang.jeongsan.model.GroupCreateItem
import com.kappzzang.jeongsan.model.GroupItem
import com.kappzzang.jeongsan.repository.GroupInfoRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GroupInfoRepositoryImpl @Inject constructor(
    private val groupRemoteDataSource: GroupRemoteDataSource
) : GroupInfoRepository {

    override suspend fun getProgressingGroupInfo(): List<GroupItem> {
        val result = groupRemoteDataSource.getGroupInfo(false)
        result.fold(
            onSuccess = {
                return it.map { it.toGroupItem() }
            },
            onFailure = {
                it.printStackTrace()
                return emptyList()
            }
        )
    }

    override suspend fun getDoneGroupInfo(): List<GroupItem> {
        val result = groupRemoteDataSource.getGroupInfo(true)
        result.fold(
            onSuccess = {
                return it.map { it.toGroupItem() }
            },
            onFailure = {
                return emptyList()
            }
        )
    }

    override fun getTargetGroupInfo(groupId: String): Flow<GroupItem> = flow {
        groupId.toLongOrNull()?.let { id ->
            groupRemoteDataSource.getTargetGroupInfo(id).fold(
                onSuccess = {
                    it.toGroupItem()
                },
                onFailure = {
                    getBlankGroupItem()
                }
            )
        }?.let {
            emit(it)
        } ?: emit(getBlankGroupItem())
    }

    override suspend fun uploadGroupInfo(createdGroup: GroupCreateItem): Long {
        val result = groupRemoteDataSource.createGroup(
            groupName = createdGroup.name,
            groupSubject = createdGroup.subject,
            groupMemberServiceIdList = createdGroup.memberServiceIdList
        )
        return result.getOrThrow()
    }

    private fun getBlankGroupItem(): GroupItem = GroupItem(
        "",
        "",
        true,
        "",
        listOf()
    )

    override suspend fun getMemberServiceIdList(groupId: String): Result<List<String>> =
        groupRemoteDataSource.getMemberServiceId(groupId.toLong()).fold(
            onSuccess = {
                return Result.success(it.toServiceIdList())
            },
            onFailure = {
                return Result.failure(it)
            }
        )

    override suspend fun completeGroup(groupId: String): Result<Boolean> =
        groupRemoteDataSource.completeGroup(groupId.toLong())

    override suspend fun joinGroup(groupId: String): Result<Boolean> =
        groupRemoteDataSource.joinGroup(groupId.toLong())
}
