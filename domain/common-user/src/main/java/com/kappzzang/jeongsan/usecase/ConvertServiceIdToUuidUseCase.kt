package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.repository.UserInfoRepository
import javax.inject.Inject

class ConvertServiceIdToUuidUseCase @Inject constructor(
    private val userInfoRepository: UserInfoRepository
) {

    suspend operator fun invoke(serviceIds: List<String>): List<String>? {
        val friendList = userInfoRepository.getFriendList()
        return serviceIds.map { serviceId ->
            friendList?.find { it.serviceId == serviceId }?.uuid ?: return null
        }
    }
}
