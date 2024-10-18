package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.model.UserItem

interface UserInfoRepository {
    suspend fun getUserInfo(): UserItem?
}
