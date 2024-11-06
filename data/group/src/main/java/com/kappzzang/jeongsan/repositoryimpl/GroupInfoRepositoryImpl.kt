package com.kappzzang.jeongsan.repositoryimpl

import android.util.Log
import com.kappzzang.jeongsan.datasource.remote.GroupRemoteDataSource
import com.kappzzang.jeongsan.mapper.GroupEntityMapper.toGroupItem
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
        val token = ""
        val result = groupRemoteDataSource.getGroupInfo(token, false)
        result.fold(
            onSuccess = {
                return it.map { it.toGroupItem() }
            },
            onFailure = {
                return emptyList()
            }
        )
    }

    override suspend fun getDoneGroupInfo(): List<GroupItem> {
        val token = ""
        val result = groupRemoteDataSource.getGroupInfo(token, true)
        result.fold(
            onSuccess = {
                return it.map { it.toGroupItem() }
            },
            onFailure = {
                return emptyList()
            }
        )
    }

    private fun getGroupItemFromEntity(entity: GroupEntity) = GroupItem(
        entity.id.toString(),
        entity.name,
        entity.isCompleted,
        entity.subject,
        if (entity.memberProfileImage == "") emptyList() else listOf(entity.memberProfileImage)
    )

    override fun getGroupInfo(groupId: String): Flow<GroupItem> = flow {
        emit(
            groupId.toLongOrNull()?.let { id ->
                groupDatabase.groupDao().inquireGroupInfo(id).firstOrNull()?.let {
                    getGroupItemFromEntity(it)
                } ?: GroupItem("0", "", false, "", emptyList())
            } ?: GroupItem("0", "", false, "", emptyList())
        )
    }

    override suspend fun uploadGroupInfo(createdGroup: GroupCreateItem) {
        val token = ""
        Log.d("GroupRepositoryImpl", createdGroup.memberUuidList.toString())
        groupRemoteDataSource.createGroup(
            jwt = token,
            groupName = createdGroup.name,
            groupSubject = createdGroup.subject,
            groupMemberId = createdGroup.memberUuidList.map { 1L }
        )
    }

    fun getBlankGroupItem(): GroupItem {
        return GroupItem("", "", true, "", listOf())
    }
}
