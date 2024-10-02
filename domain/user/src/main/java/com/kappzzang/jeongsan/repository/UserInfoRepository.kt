package com.kappzzang.jeongsan.repository

interface UserInfoRepository {
    suspend fun getUserInfo(): com.kappzzang.jeongsan.model.UserItem
}
