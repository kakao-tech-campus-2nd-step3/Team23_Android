package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.data.KakaoAuthData
import com.kappzzang.jeongsan.repository.ServerAuthenticationRepository
import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject

class LoginWithTestAccountUseCase @Inject constructor(
    private val serverAuthenticationRepository: ServerAuthenticationRepository,
    private val authenticationRepository: AuthenticationRepository,
) {

    private fun createTestKakaoAuthData() = KakaoAuthData(
        kakaoAccessToken = "testKakaoAccessToken",
        kakaoRefreshToken = "testKakaoRefreshToken",
        accessTokenExpirationTime = 5000000L
    )

    suspend operator fun invoke(testAccount: String, testServiceId: String) =
        serverAuthenticationRepository.loginToServer(testAccount).onSuccess { authData ->
            authenticationRepository.updateKakaoAuthData(createTestKakaoAuthData())
            authenticationRepository.updateServerAuthData(authData)
            authenticationRepository.updateServiceId(testServiceId)
        }
}
