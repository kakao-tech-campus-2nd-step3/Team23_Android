package com.kappzzang.jeongsan.domain.usecase

import com.kappzzang.jeongsan.domain.model.UserItem
import com.kappzzang.jeongsan.domain.repository.UserInfoRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(private val userInfoRepository: UserInfoRepository) {
    suspend operator fun invoke(): UserItem = userInfoRepository.getUserInfo()
}
