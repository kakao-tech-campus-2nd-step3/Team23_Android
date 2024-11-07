package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.data.KakaoAuthData
import com.kappzzang.jeongsan.data.ServerAuthData
import com.kappzzang.jeongsan.datasource.AuthLocalDataSource
import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class AuthenticationRepositoryImpl
@Inject constructor(
    private val datasource: AuthLocalDataSource
) : AuthenticationRepository {

    override fun getKakaoAuthData(): Flow<KakaoAuthData> = datasource.getKakaoAuthDataFlow()

    override fun getServerAuthData(): Flow<ServerAuthData> = datasource.getServerAuthDataFlow()

    override suspend fun updateKakaoAuthData(newData: KakaoAuthData) {
        datasource.updateKakaoPreference(newData)
    }

    override suspend fun updateServerAuthData(newData: ServerAuthData) {
        datasource.updateServerPreference(newData)
    }

    override suspend fun removeKakaoAuthData() {
        TODO("Not yet implemented")
    }

    override suspend fun removeServerAuthData() {
        TODO("Not yet implemented")
    }
}
