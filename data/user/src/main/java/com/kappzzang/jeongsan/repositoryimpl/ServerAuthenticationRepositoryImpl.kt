package com.kappzzang.jeongsan.repositoryimpl

import android.util.Log
import com.kappzzang.jeongsan.data.ServerAuthData
import com.kappzzang.jeongsan.datasource.ServerAuthRemoteDataSource
import com.kappzzang.jeongsan.mapper.TokenDataToServerAuthDataMapper
import com.kappzzang.jeongsan.repository.ServerAuthenticationRepository
import javax.inject.Inject

class ServerAuthenticationRepositoryImpl @Inject constructor(
    private val dataSource: ServerAuthRemoteDataSource
) : ServerAuthenticationRepository {

    override suspend fun loginToServer(email: String): Result<ServerAuthData> =
        dataSource.login(email).fold(
            onSuccess = { tokenData ->
                val serverAuthData =
                    TokenDataToServerAuthDataMapper.mapTokenDataToServerAuthData(tokenData)
                Result.success(serverAuthData)
            },
            onFailure = { exception ->
                Log.e(TAG, exception.message, exception.cause)
                Result.failure(exception)
            }
        )

    override suspend fun registerToServer(
        nickname: String,
        email: String,
        profileImageUrl: String
    ): Result<ServerAuthData> = dataSource.register(nickname, email, profileImageUrl).fold(
        onSuccess = { tokenData ->
            val serverAuthData =
                TokenDataToServerAuthDataMapper.mapTokenDataToServerAuthData(tokenData)
            Result.success(serverAuthData)
        },
        onFailure = { exception ->
            Log.e(TAG, exception.message, exception.cause)
            Result.failure(exception)
        }
    )

    companion object {
        private const val TAG = "ServerAuthenticationRepositoryImpl"
    }
}
