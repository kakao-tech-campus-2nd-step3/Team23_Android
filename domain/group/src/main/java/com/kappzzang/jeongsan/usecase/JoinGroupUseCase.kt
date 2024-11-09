package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.repository.GroupInfoRepository
import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject

class JoinGroupUseCase @Inject constructor(
    private val groupInfoRepository: GroupInfoRepository,
    private val authenticationRepository: AuthenticationRepository
) {

    suspend operator fun invoke(groupId: String): Result<Boolean> {
        val myServiceId = authenticationRepository.getServiceId()
        return groupInfoRepository.joinGroup(groupId, myServiceId)
    }
}
