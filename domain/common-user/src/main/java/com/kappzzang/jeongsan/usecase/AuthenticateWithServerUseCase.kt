package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.data.ServerAuthData
import com.kappzzang.jeongsan.repository.ServerAuthenticationRepository
import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject

class AuthenticateWithServerUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val serverAuthenticationRepository: ServerAuthenticationRepository
) {

    suspend operator fun invoke(nickname: String, email: String, profileImageUrl: String) {
        val authData = attemptLoginOrRegister(nickname, email, profileImageUrl)
        authenticationRepository.updateServerAuthData(authData)
    }

    private suspend fun attemptLoginOrRegister(
        nickname: String,
        email: String,
        profileImageUrl: String
    ): ServerAuthData = serverAuthenticationRepository.loginToServer(email).getOrElse { exception ->
        when (exception) {
            is NoSuchElementException -> registerToServer(nickname, email, profileImageUrl)
            else -> throw exception
        }
    }

    private suspend fun registerToServer(
        nickname: String,
        email: String,
        profileImageUrl: String
    ): ServerAuthData =
        serverAuthenticationRepository.registerToServer(nickname, email, profileImageUrl)
            .getOrThrow()
}
