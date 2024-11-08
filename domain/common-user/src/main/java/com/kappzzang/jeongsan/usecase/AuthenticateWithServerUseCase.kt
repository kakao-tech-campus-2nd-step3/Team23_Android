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
        uuid: String,
        nickname: String,
        email: String,
        profileImageUrl: String
    ) {
        val authData = attemptLoginOrRegister(uuid, nickname, email, profileImageUrl)
        authenticationRepository.updateServerAuthData(authData)
    }

    private suspend fun attemptLoginOrRegister(
        uuid: String,
        nickname: String,
        email: String,
        profileImageUrl: String
    ): ServerAuthData = serverAuthenticationRepository.loginToServer(email).getOrElse { exception ->
        when (exception) {
            is NoSuchElementException -> registerToServer(uuid, nickname, email, profileImageUrl)
            else -> throw exception
        }
    }

    private suspend fun registerToServer(
        uuid: String,
        nickname: String,
        email: String,
        profileImageUrl: String
    ): ServerAuthData =
        serverAuthenticationRepository.registerToServer(uuid, nickname, email, profileImageUrl)
            .getOrThrow()
}
