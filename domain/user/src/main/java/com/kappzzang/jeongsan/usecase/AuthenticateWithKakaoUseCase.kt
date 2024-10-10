package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.data.AuthData
import com.kappzzang.jeongsan.model.AuthenticationResult
import com.kappzzang.jeongsan.repository.KakaoAuthenticationRepository
import com.kappzzang.jeongsan.util.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthenticateWithKakaoUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val kakaoAuthenticationRepository: KakaoAuthenticationRepository
) {
    private fun updateAccessToken(old: AuthData, new: AuthData): AuthData {
        return if(new.kakaoRefreshToken.isEmpty()){
            new.copy(
                kakaoRefreshToken = old.kakaoRefreshToken,
                jwt = old.jwt
            )
        } else{
            new.copy(
                jwt = old.jwt
            )
        }
    }

    private fun getCurrentTime(): Long =
        System.currentTimeMillis()

    private fun checkNeedToRefresh(data: AuthData): Boolean =
        (data.accessTokenExpirationTime - getCurrentTime()) < REFRESH_TIME_WITHIN_MILLISECONDS

    private fun checkIsEmptyAuthData(authData: AuthData): Boolean =
        authData.kakaoAccessToken == ""

    operator fun invoke(): Flow<AuthenticationResult> {
        val authDataFlow = authenticationRepository.getAuthData()

        return authDataFlow.map { authData ->
            if(checkIsEmptyAuthData(authData)){
                AuthenticationResult.NoToken
            }
            else{
                if(checkNeedToRefresh(authData)){
                    val newData = kakaoAuthenticationRepository.refreshKakaoToken(authData)
                    val updateData = updateAccessToken(authData, newData)

                    authenticationRepository.updateAuthData(updateData)
                    AuthenticationResult.AuthenticationSuccess(updateData)
                }
                else{
                    AuthenticationResult.AuthenticationSuccess(authData)
                }
            }
        }
    }

    companion object {
        private const val REFRESH_TIME_WITHIN_MILLISECONDS = 600_000L
    }
}
