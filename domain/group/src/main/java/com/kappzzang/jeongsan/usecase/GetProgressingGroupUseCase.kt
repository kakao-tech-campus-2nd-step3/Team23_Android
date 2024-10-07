package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.GroupItem
import com.kappzzang.jeongsan.repository.GroupInfoRepository
import javax.inject.Inject

class GetProgressingGroupUseCase @Inject constructor(
    private val groupInfoRepository: GroupInfoRepository
) {
    suspend operator fun invoke(): List<GroupItem> = groupInfoRepository.getProgressingGroupInfo()
}
