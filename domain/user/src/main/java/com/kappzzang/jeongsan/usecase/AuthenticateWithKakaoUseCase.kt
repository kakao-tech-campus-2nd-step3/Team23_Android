package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.data.AuthData
import com.kappzzang.jeongsan.model.AuthenticationResult
import com.kappzzang.jeongsan.repository.KakaoAuthenticationRepository
import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject

class AuthenticateWithKakaoUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val kakaoAuthenticationRepository: KakaoAuthenticationRepository
) {
    private fun getCurrentTime(): Long =
        System.currentTimeMillis()

    private fun checkNeedToRefresh(data: AuthData): Boolean =
        (data.accessTokenExpirationTime - getCurrentTime()) < REFRESH_TIME_WITHIN_MILLISECONDS

    suspend operator fun invoke(): AuthenticationResult {
        //TODO: implement
        return AuthenticationResult.NoToken
        //if(!authenticationRepository.checkHasAuthData()){
        //    return AuthenticationResult.NoToken
        //}
//
        //val authData = authenticationRepository.getAuthData()
        //if(checkNeedToRefresh(authData)){
        //    val newData = kakaoAuthenticationRepository.refreshKakaoToken(authData)
        //    authenticationRepository.updateAuthData(newData)
//
        //    return AuthenticationResult.AuthenticationSuccess(
        //        authData = authenticationRepository.getAuthData()
        //    )
        //}
        //return AuthenticationResult.AuthenticationSuccess(
        //    authData = authData
        //)
    }

    companion object {
        private const val REFRESH_TIME_WITHIN_MILLISECONDS = 120_000L
    }
}
