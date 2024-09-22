package com.kappzzang.jeongsan.domain.repository

import com.kappzzang.jeongsan.domain.model.UserItem

interface UserInfoRepository {
    suspend fun getUserInfo(): UserItem
}
