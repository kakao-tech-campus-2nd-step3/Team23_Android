package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject

class GetUserUuidUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {

    operator fun invoke(): String = authenticationRepository.getUuid()
}
