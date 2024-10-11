package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.GroupCreateItem
import com.kappzzang.jeongsan.repository.GroupInfoRepository
import javax.inject.Inject

class UploadGroupInfoUseCase @Inject constructor(
    private val groupInfoRepository: GroupInfoRepository
) {
    suspend operator fun invoke(createdGroup: GroupCreateItem) {
        groupInfoRepository.uploadGroupInfo(createdGroup)
    }
}
