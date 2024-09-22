package com.kappzzang.jeongsan.domain.repository

import com.kappzzang.jeongsan.domain.model.GroupItem

interface GroupInfoRepository {
    suspend fun getProgressingGroupInfo(): List<GroupItem>
    suspend fun getDoneGroupInfo(): List<GroupItem>
}
