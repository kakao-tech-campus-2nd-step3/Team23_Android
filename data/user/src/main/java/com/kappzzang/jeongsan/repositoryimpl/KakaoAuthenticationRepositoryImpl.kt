package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.data.AuthData
import com.kappzzang.jeongsan.repository.KakaoAuthenticationRepository

internal class KakaoAuthenticationRepositoryImpl: KakaoAuthenticationRepository {
    override suspend fun refreshKakaoToken(authData: AuthData): AuthData {
        TODO("Not yet implemented")
    }
}