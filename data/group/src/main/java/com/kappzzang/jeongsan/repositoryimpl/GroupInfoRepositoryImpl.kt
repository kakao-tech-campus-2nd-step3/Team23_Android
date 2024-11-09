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
        val result = groupRemoteDataSource.getGroupInfo(false)
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
        Log.d("GroupRepositoryImpl", createdGroup.memberUuidList.toString())
        val result = groupRemoteDataSource.createGroup(
            groupName = createdGroup.name,
            groupSubject = createdGroup.subject,
            // groupMemberUuidList = createdGroup.memberUuidList
            // TODO 멤버 UUID가 null로 들어가 있어서 제대로 처리가 안되는 오류가 있다고 합니다.
            // TODO 서버 내에 저장된 더미 데이터인 UUID 1 값을 넣게 임시조치 했습니다.
            groupMemberUuidList = listOf("1")
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
}
