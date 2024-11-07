package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.data.KakaoAuthData
import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject

class AuthorizeWithKakaoUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(authData: KakaoAuthData) {
        authenticationRepository.updateKakaoAuthData(authData)
    }
}
