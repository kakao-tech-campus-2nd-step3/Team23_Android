package com.kappzzang.jeongsan.datasource

import com.kappzzang.jeongsan.api.ServerAuthRetrofitService
import com.kappzzang.jeongsan.entity.AuthResponse
import com.kappzzang.jeongsan.entity.LoginRequest
import com.kappzzang.jeongsan.entity.RefreshResponse
import com.kappzzang.jeongsan.entity.RefreshTokenData
import com.kappzzang.jeongsan.entity.RefreshTokenRequest
import com.kappzzang.jeongsan.entity.RegisterRequest
import com.kappzzang.jeongsan.entity.TokenData
import javax.inject.Inject
import retrofit2.Response

class ServerAuthRemoteDataSource @Inject constructor(
    private val authApi: ServerAuthRetrofitService
) {

    suspend fun refreshToken(refreshToken: String): Result<RefreshTokenData> = try {
        val response = authApi.refreshToken(RefreshTokenRequest(refreshToken = refreshToken))
        handleRefreshResponse(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // 리프레시 할 때 Response를 처리
    private fun handleRefreshResponse(
        response: Response<RefreshResponse>
    ): Result<RefreshTokenData> = when {
        response.isSuccessful && response.body()?.data != null -> {
            Result.success(response.body()!!.data)
        }

        response.code() == 403 -> {
            Result.failure(SecurityException("리프레시 토큰이 유효하지 않음."))
        }

        response.code() == 404 -> {
            Result.failure(NoSuchElementException("사용자를 찾을 수 없음."))
        }

        else -> {
            Result.failure(
                Exception("Unexpected Error: ${response.code()} - ${response.message()}")
            )
        }
    }

    suspend fun register(nickname: String, email: String, profileUrl: String): Result<TokenData> =
        try {
            val response = authApi.register(
                RegisterRequest(nickname = nickname, email = email, profileImageUrl = profileUrl)
            )
            handleRegisterResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }

    // 회원가입 시 Response를 처리
    private fun handleRegisterResponse(response: Response<AuthResponse>): Result<TokenData> = when {
        response.isSuccessful && response.body()?.data != null -> {
            Result.success(response.body()!!.data)
        }

        response.code() == 400 -> {
            Result.failure(IllegalStateException("해당 사용자는 이미 회원가입됨."))
        }

        else -> {
            Result.failure(
                Exception("Unexpected error: ${response.code()} - ${response.message()}")
            )
        }
    }

    suspend fun login(email: String): Result<TokenData> = try {
        val response = authApi.login(LoginRequest(email = email))
        handleLoginResponse(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // 로그인 시 Response를 처리
    private fun handleLoginResponse(response: Response<AuthResponse>): Result<TokenData> = when {
        response.isSuccessful && response.body()?.data != null -> {
            Result.success(response.body()!!.data)
        }

        response.code() == 404 -> {
            Result.failure(NoSuchElementException("사용자를 찾을 수 없음."))
        }

        else -> {
            Result.failure(
                Exception("Unexpected error: ${response.code()} - ${response.message()}")
            )
        }
    }
}
