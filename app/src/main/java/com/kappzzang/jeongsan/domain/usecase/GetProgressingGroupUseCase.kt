package com.kappzzang.jeongsan.domain.usecase

import com.kappzzang.jeongsan.domain.model.GroupItem
import com.kappzzang.jeongsan.domain.repository.GroupInfoRepository

class GetProgressingGroupUseCase(private val groupInfoRepository: GroupInfoRepository) {
    suspend operator fun invoke(): List<GroupItem> = groupInfoRepository.getProgressingGroupInfo()
}
