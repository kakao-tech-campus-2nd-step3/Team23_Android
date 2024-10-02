package com.kappzzang.jeongsan.usecase

import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userInfoRepository: com.kappzzang.jeongsan.repository.UserInfoRepository
) {
    suspend operator fun invoke(): com.kappzzang.jeongsan.model.UserItem =
        userInfoRepository.getUserInfo()
}
