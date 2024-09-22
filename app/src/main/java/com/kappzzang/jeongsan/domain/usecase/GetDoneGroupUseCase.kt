package com.kappzzang.jeongsan.domain.usecase

import com.kappzzang.jeongsan.domain.model.GroupItem
import com.kappzzang.jeongsan.domain.repository.GroupInfoRepository
import javax.inject.Inject

class GetDoneGroupUseCase @Inject constructor(
    private val groupInfoRepository: GroupInfoRepository
) {
    suspend operator fun invoke(): List<GroupItem> = groupInfoRepository.getDoneGroupInfo()
}
