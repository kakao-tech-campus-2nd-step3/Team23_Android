package com.kappzzang.jeongsan.domain.usecase

import com.kappzzang.jeongsan.domain.repository.UserInfoRepository
import javax.inject.Inject

class GetUserNameUseCase @Inject constructor(private val userInfoRepository: UserInfoRepository) {
    suspend operator fun invoke(): String = userInfoRepository.getUserName()
}
