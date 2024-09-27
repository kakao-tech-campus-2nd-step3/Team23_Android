package com.kappzzang.jeongsan.data.repositoryimpl

import com.kappzzang.jeongsan.data.datasource.group.GroupDatabase
import com.kappzzang.jeongsan.data.entity.GroupEntity
import com.kappzzang.jeongsan.domain.model.GroupItem
import com.kappzzang.jeongsan.domain.repository.GroupInfoRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GroupInfoRepositoryImpl @Inject constructor(private val groupDatabase: GroupDatabase) :
    GroupInfoRepository {

    override suspend fun getProgressingGroupInfo(): List<GroupItem> {
        // TODO: 임시로 테스트 데이터 반환 - 이후에 서버에서 받아오도록 수정
        return groupDatabase.groupDao().getProgressingGroup().map { getGroupItemFromEntity(it) }
    }

    override suspend fun getDoneGroupInfo(): List<GroupItem> {
        // TODO: 임시로 테스트 데이터 반환 - 이후에 서버에서 받아오도록 수정
        return groupDatabase.groupDao().getDoneGroup().map { getGroupItemFromEntity(it) }
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
}
