package com.kappzzang.jeongsan.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCurrentGroupInfoUseCase @Inject constructor(
    private val repository: com.kappzzang.jeongsan.repository.GroupInfoRepository
) {
    operator fun invoke(groupId: String): Flow<com.kappzzang.jeongsan.model.GroupItem> =
        repository.getGroupInfo(groupId)
}
