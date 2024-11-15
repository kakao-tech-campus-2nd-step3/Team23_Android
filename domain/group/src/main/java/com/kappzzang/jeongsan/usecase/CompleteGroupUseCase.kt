package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.repository.GroupInfoRepository
import javax.inject.Inject

class CompleteGroupUseCase @Inject constructor(
    private val groupInfoRepository: GroupInfoRepository
) {

    suspend operator fun invoke(groupId: String) = groupInfoRepository.completeGroup(groupId)
}
