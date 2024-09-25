package com.kappzzang.jeongsan.domain.repository

import com.kappzzang.jeongsan.domain.model.GroupItem
import kotlinx.coroutines.flow.Flow

interface GroupInfoRepository {

    suspend fun getProgressingGroupInfo(): List<GroupItem>
    suspend fun getDoneGroupInfo(): List<GroupItem>

    /**
     *  현재 조회중인 그룹 내용 조회
     *  @param groupId 그룹명
     *  @return 그룹 정보
     */
    fun getGroupInfo(groupId: String): Flow<GroupItem>
}
