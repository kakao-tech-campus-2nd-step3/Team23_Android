package com.kappzzang.jeongsan

import com.kappzzang.jeongsan.entity.TokenData
import com.kappzzang.jeongsan.mapper.TokenDataToServerAuthDataMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class TokenDataToServerAuthDataMapperTest {

    @Test
    fun `TokenData 객체가 ServerAuthData 객체로 올바르게 매핑된다`() {
        // given
        val tokenData = TokenData(
            tokenType = "Bearer",
            accessToken = "testAccessToken",
            refreshToken = "testRefreshToken"
        )

        // when
        val serverAuthData = TokenDataToServerAuthDataMapper.mapTokenDataToServerAuthData(tokenData)

        // then
        assertEquals("testAccessToken", serverAuthData.accessToken)
        assertEquals("testRefreshToken", serverAuthData.refreshToken)
    }
}
