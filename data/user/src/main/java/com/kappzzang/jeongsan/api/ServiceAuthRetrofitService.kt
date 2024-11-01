package com.kappzzang.jeongsan.api

import com.kappzzang.jeongsan.entity.AuthResponse
import com.kappzzang.jeongsan.entity.LoginRequest
import com.kappzzang.jeongsan.entity.RefreshTokenRequest
import com.kappzzang.jeongsan.entity.RefreshResponse
import com.kappzzang.jeongsan.entity.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

interface ServiceAuthRetrofitService {

    @POST("/api/members/token/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Response<RefreshResponse>

    @POST("/api/members/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("/api/members/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>
}
