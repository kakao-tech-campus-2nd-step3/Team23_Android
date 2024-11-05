package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.data.ServerAuthData
import com.kappzzang.jeongsan.entity.TokenData

object TokenDataToServerAuthDataMapper {

    fun mapTokenDataToServerAuthData(tokenData: TokenData): ServerAuthData {
        return ServerAuthData(
            accessToken = tokenData.accessToken,
            refreshToken = tokenData.refreshToken
        )
    }
}