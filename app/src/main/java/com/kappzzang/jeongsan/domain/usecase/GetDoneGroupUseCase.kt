package com.kappzzang.jeongsan.domain.usecase

import com.kappzzang.jeongsan.domain.model.GroupItem
import com.kappzzang.jeongsan.domain.repository.GroupInfoRepository

class GetDoneGroupUseCase(private val groupInfoRepository: GroupInfoRepository) {
    suspend operator fun invoke(): List<GroupItem> = groupInfoRepository.getDoneGroupInfo()
}
