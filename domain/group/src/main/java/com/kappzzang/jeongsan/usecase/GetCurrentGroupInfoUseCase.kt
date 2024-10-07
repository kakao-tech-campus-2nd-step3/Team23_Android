package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.GroupItem
import com.kappzzang.jeongsan.repository.GroupInfoRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCurrentGroupInfoUseCase @Inject constructor(private val repository: GroupInfoRepository) {
    operator fun invoke(groupId: String): Flow<GroupItem> = repository.getGroupInfo(groupId)
}
