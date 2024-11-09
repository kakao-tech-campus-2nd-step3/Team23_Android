package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.GroupCreateItem
import com.kappzzang.jeongsan.repository.GroupInfoRepository
import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject

class UploadGroupInfoUseCase @Inject constructor(
    private val groupInfoRepository: GroupInfoRepository,
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(createdGroup: GroupCreateItem): Long {
        val ownerServiceId = authenticationRepository.getServiceId()
        createdGroup.memberServiceIdList.toMutableList().add(ownerServiceId)

        return groupInfoRepository.uploadGroupInfo(createdGroup)
    }
}
