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

    override suspend fun loginToServer(email: String): ServerAuthData {
        val result = dataSource.login(email)

        return when (result.isSuccess) {
            true -> {
                TokenDataToServerAuthDataMapper.mapTokenDataToServerAuthData(result.getOrThrow())
            }

            false -> {
                val exception = result.exceptionOrNull() ?: Exception("Unknown Error")
                Log.e(TAG, exception.message, exception.cause)
                throw exception
            }
        }
    }

    override suspend fun registerToServer(
        nickname: String,
        email: String,
        profileImageUrl: String
    ): ServerAuthData {
        val result = dataSource.register(nickname, email, profileImageUrl)

        return when (result.isSuccess) {
            true -> {
                TokenDataToServerAuthDataMapper.mapTokenDataToServerAuthData(result.getOrThrow())
            }

            false -> {
                val exception = result.exceptionOrNull() ?: Exception("Unknown Error")
                Log.e(TAG, exception.message, exception.cause)
                throw exception
            }
        }
    }

    override suspend fun refreshJwtFromServer(authData: ServerAuthData): ServerAuthData {
        val result = dataSource.refreshToken(authData.refreshToken)

        return when (result.isSuccess) {
            true -> {
                authData.copy(
                    accessToken = result.getOrThrow().accessToken
                )
            }

            false -> {
                val exception = result.exceptionOrNull() ?: Exception("Unknown Error")
                Log.e(TAG, exception.message, exception.cause)
                throw exception
            }
        }
    }

    companion object {
        private const val TAG = "ServerAuthenticationRepositoryImpl"
    }
}
