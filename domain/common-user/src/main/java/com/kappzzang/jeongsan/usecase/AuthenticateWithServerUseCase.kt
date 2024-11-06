package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.repository.ServerAuthenticationRepository
import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject

class AuthenticateWithServerUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val serverAuthenticationRepository: ServerAuthenticationRepository
) {

    suspend operator fun invoke(nickname: String, email: String, profileImageUrl: String) {
        val authData = try {
            serverAuthenticationRepository.loginToServer(email)
        } catch (e: NoSuchElementException) {
            serverAuthenticationRepository.registerToServer(nickname, email, profileImageUrl)
        }

        authenticationRepository.updateServerAuthData(authData)
    }
}
