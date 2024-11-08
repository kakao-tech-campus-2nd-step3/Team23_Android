package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject

class GetUserServiceIdUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {

    operator fun invoke(): String = authenticationRepository.getServiceId()
}
