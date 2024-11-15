package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject

class AuthenticateWithServerUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {

    operator fun invoke() = authenticationRepository.getServerAuthData()
}
