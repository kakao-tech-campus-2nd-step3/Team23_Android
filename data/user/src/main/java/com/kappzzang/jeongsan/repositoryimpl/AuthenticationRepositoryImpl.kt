package com.kappzzang.jeongsan.repositoryimpl

import android.util.Log
import com.kappzzang.jeongsan.data.KakaoAuthData
import com.kappzzang.jeongsan.data.ServerAuthData
import com.kappzzang.jeongsan.datasource.AuthLocalDataSource
import com.kappzzang.jeongsan.datasource.ServerAuthRemoteDataSource
import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject

class AuthenticationRepositoryImpl
@Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource,
    private val serverAuthRemoteDataSource: ServerAuthRemoteDataSource
) : AuthenticationRepository {

    override fun getKakaoAuthData(): KakaoAuthData = authLocalDataSource.getKakaoAuthData()

    override fun getServerAuthData(): ServerAuthData = authLocalDataSource.getServerAuthData()

    override fun updateKakaoAuthData(newData: KakaoAuthData) {
        authLocalDataSource.updateKakaoPreference(newData)
    }

    override fun updateServerAuthData(newData: ServerAuthData) {
        authLocalDataSource.updateServerPreference(newData)
    }

    override fun removeKakaoAuthData() {
        authLocalDataSource.removeKakaoAuthData()
    }

    override fun removeServerAuthData() {
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

    override fun getServiceId(): String = authLocalDataSource.getServiceId()

    override fun updateServiceId(uuid: String) {
        authLocalDataSource.updateServiceId(uuid)
    }

    override fun removeServiceId() {
        authLocalDataSource.removeServiceId()
    }

    companion object {
        private const val TAG = "AuthenticationRepositoryImpl"
    }
}
