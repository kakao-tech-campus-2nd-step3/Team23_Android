package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.data.AuthData
import com.kappzzang.jeongsan.repository.KakaoAuthenticationRepository
import javax.inject.Inject

class KakaoAuthenticationRepositoryImpl @Inject constructor(): KakaoAuthenticationRepository {
    override suspend fun refreshKakaoToken(authData: AuthData): AuthData {
        TODO("Not yet implemented")
    }
}