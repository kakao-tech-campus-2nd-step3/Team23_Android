package com.kappzzang.jeongsan.repository

import kotlinx.coroutines.flow.Flow

interface GroupInfoRepository {

    suspend fun getProgressingGroupInfo(): List<com.kappzzang.jeongsan.model.GroupItem>
    suspend fun getDoneGroupInfo(): List<com.kappzzang.jeongsan.model.GroupItem>

    /**
     *  현재 조회중인 그룹 내용 조회
     *  @param groupId 그룹명
     *  @return 그룹 정보
     */
    fun getGroupInfo(groupId: String): Flow<com.kappzzang.jeongsan.model.GroupItem>
}
