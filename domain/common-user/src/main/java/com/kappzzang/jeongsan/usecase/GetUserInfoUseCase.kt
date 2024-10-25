package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.UserItem
import com.kappzzang.jeongsan.repository.UserInfoRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(private val userInfoRepository: UserInfoRepository) {
    suspend operator fun invoke(): UserItem? = userInfoRepository.getUserInfo()
}
