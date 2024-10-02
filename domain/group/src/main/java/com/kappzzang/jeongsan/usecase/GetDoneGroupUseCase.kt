package com.kappzzang.jeongsan.usecase

import javax.inject.Inject

class GetDoneGroupUseCase @Inject constructor(
    private val groupInfoRepository: com.kappzzang.jeongsan.repository.GroupInfoRepository
) {
    suspend operator fun invoke(): List<com.kappzzang.jeongsan.model.GroupItem> =
        groupInfoRepository.getDoneGroupInfo()
}
