package com.kappzzang.jeongsan.domain.usecase

import com.kappzzang.jeongsan.domain.model.GroupItem
import com.kappzzang.jeongsan.domain.repository.GroupInfoRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCurrentGroupInfoUseCase @Inject constructor(private val repository: GroupInfoRepository) {
    operator fun invoke(groupId: String): Flow<GroupItem> = repository.getGroupInfo(groupId)
}
