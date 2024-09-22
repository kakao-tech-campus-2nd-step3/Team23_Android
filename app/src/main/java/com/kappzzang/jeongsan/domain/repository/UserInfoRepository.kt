package com.kappzzang.jeongsan.domain.repository

interface UserInfoRepository {
    suspend fun getUserName(): String
}
