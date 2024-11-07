package com.kappzzang.jeongsan.api

import com.kappzzang.jeongsan.entity.AuthResponse
import com.kappzzang.jeongsan.entity.LoginRequest
import com.kappzzang.jeongsan.entity.RefreshResponse
import com.kappzzang.jeongsan.entity.RefreshTokenRequest
import com.kappzzang.jeongsan.entity.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ServerAuthRetrofitService {

    @POST("/api/members/token/refresh")
    @Headers("Auth: false")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<RefreshResponse>

    @POST("/api/members/register")
    @Headers("Auth: false")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("/api/members/login")
    @Headers("Auth: false")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
}
