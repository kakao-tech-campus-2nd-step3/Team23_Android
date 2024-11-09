package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.data.ServerAuthData
import com.kappzzang.jeongsan.repository.ServerAuthenticationRepository
import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject

class AuthenticateWithServerUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val serverAuthenticationRepository: ServerAuthenticationRepository
) {

    suspend operator fun invoke(
        serviceId: String,
        nickname: String,
        email: String,
        profileImageUrl: String
    ) {
        val authData = attemptLoginOrRegister(serviceId, nickname, email, profileImageUrl)
        authenticationRepository.updateServerAuthData(authData)
        authenticationRepository.updateServiceId(serviceId)
    }

    private suspend fun attemptLoginOrRegister(
        serviceId: String,
        nickname: String,
        email: String,
        profileImageUrl: String
    ): ServerAuthData = serverAuthenticationRepository.loginToServer(email).getOrElse { exception ->
        when (exception) {
            is NoSuchElementException -> registerToServer(serviceId, nickname, email, profileImageUrl)
            else -> throw exception
        }
    }

    private suspend fun registerToServer(
        serviceId: String,
        nickname: String,
        email: String,
        profileImageUrl: String
    ): ServerAuthData =
        serverAuthenticationRepository.registerToServer(serviceId, nickname, email, profileImageUrl)
            .getOrThrow()
}
