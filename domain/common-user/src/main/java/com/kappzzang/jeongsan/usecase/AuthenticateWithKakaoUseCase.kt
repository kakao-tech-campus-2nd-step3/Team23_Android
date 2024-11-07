package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.data.KakaoAuthData
import com.kappzzang.jeongsan.model.AuthenticationResult
import com.kappzzang.jeongsan.repository.KakaoAuthenticationRepository
import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthenticateWithKakaoUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val kakaoAuthenticationRepository: KakaoAuthenticationRepository
) {
    private fun updateAccessToken(old: KakaoAuthData, new: KakaoAuthData): KakaoAuthData =
        if (new.kakaoRefreshToken.isEmpty()) {
            new.copy(
                kakaoRefreshToken = old.kakaoRefreshToken
            )
        } else {
            new.copy()
        }

    private fun getCurrentTime(): Long = System.currentTimeMillis()

    private fun checkNeedToRefresh(data: KakaoAuthData): Boolean =
        (data.accessTokenExpirationTime - getCurrentTime()) < REFRESH_TIME_WITHIN_MILLISECONDS

    private fun checkIsEmptyAuthData(authData: KakaoAuthData): Boolean =
        authData.kakaoAccessToken == ""

    operator fun invoke(): Flow<AuthenticationResult> {
        val authDataFlow = authenticationRepository.getKakaoAuthData()

        return authDataFlow.map { authData ->
            if (checkIsEmptyAuthData(authData)) {
                AuthenticationResult.NoToken
            } else {
                if (checkNeedToRefresh(authData)) {
                    val newData = kakaoAuthenticationRepository.refreshKakaoToken(authData)
                    val updateData = updateAccessToken(authData, newData)

                    authenticationRepository.updateKakaoAuthData(updateData)
                    AuthenticationResult.AuthenticationSuccess(updateData)
                } else {
                    AuthenticationResult.AuthenticationSuccess(authData)
                }
            }
        }
    }

    companion object {
        private const val REFRESH_TIME_WITHIN_MILLISECONDS = 600_000L
    }
}
