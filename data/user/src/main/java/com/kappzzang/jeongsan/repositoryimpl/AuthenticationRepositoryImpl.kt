package com.kappzzang.jeongsan.repositoryimpl

import android.util.Log
import com.kappzzang.jeongsan.data.KakaoAuthData
import com.kappzzang.jeongsan.data.ServerAuthData
import com.kappzzang.jeongsan.datasource.AuthLocalDataSource
import com.kappzzang.jeongsan.datasource.ServerAuthRemoteDataSource
import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class AuthenticationRepositoryImpl
@Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource,
    private val serverAuthRemoteDataSource: ServerAuthRemoteDataSource
) : AuthenticationRepository {

    override fun getKakaoAuthData(): Flow<KakaoAuthData> = authLocalDataSource.getKakaoAuthDataFlow()

    override fun getServerAuthData(): Flow<ServerAuthData> = authLocalDataSource.getServerAuthDataFlow()

    override suspend fun updateKakaoAuthData(newData: KakaoAuthData) {
        authLocalDataSource.updateKakaoPreference(newData)
    }

    override suspend fun updateServerAuthData(newData: ServerAuthData) {
        authLocalDataSource.updateServerPreference(newData)
    }

    override suspend fun removeKakaoAuthData() {
        authLocalDataSource.removeKakaoAuthData()
    }

    override suspend fun removeServerAuthData() {
        authLocalDataSource.removeServerAuthData()
    }

    override suspend fun refreshJwtFromServer(authData: ServerAuthData): Result<ServerAuthData> =
        serverAuthRemoteDataSource.refreshToken(authData.refreshToken).fold(
            onSuccess = { refreshTokenData ->
                val serverAuthData = authData.copy(
                    accessToken = refreshTokenData.accessToken
                )
                Result.success(serverAuthData)
            },
            onFailure = { exception ->
                Log.e(TAG, exception.message, exception.cause)
                Result.failure(exception)
            }
        )

    companion object {
        private const val TAG = "AuthenticationRepositoryImpl"
    }
}
