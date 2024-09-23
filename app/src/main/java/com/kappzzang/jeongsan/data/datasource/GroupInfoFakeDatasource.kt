package com.kappzzang.jeongsan.data.datasource

import com.kappzzang.jeongsan.domain.model.GroupItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GroupInfoFakeDatasource @Inject constructor(){

    fun getGroupInfo(): Flow<GroupItem> = flow {
        emit(GroupItem("1", "KAPP짱", false, listOf()))
    }
}
