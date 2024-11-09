package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.model.GroupCreateItem
import com.kappzzang.jeongsan.model.GroupItem
import kotlinx.coroutines.flow.Flow

interface GroupInfoRepository {

    suspend fun getProgressingGroupInfo(): List<GroupItem>
    suspend fun getDoneGroupInfo(): List<GroupItem>

    /**
     *  현재 조회중인 그룹 내용 조회
     *  @param groupId 그룹명
     *  @return 그룹 정보
     */
    fun getTargetGroupInfo(groupId: String): Flow<GroupItem>

    suspend fun uploadGroupInfo(createdGroup: GroupCreateItem): Long

    suspend fun completeGroup(groupId: String): Result<Boolean>
}
