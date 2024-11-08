package com.kappzzang.jeongsan.datasource

import android.content.SharedPreferences
import androidx.core.content.edit
import com.kappzzang.jeongsan.data.KakaoAuthData
import com.kappzzang.jeongsan.data.ServerAuthData
import javax.inject.Inject

class AuthLocalDataSource @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun getKakaoAuthData() = KakaoAuthData(
        kakaoAccessToken = sharedPreferences.getString(KAKAO_ACCESS_TOKEN, "") ?: "",
        kakaoRefreshToken = sharedPreferences.getString(KAKAO_REFRESH_TOKEN, "") ?: "",
        accessTokenExpirationTime = sharedPreferences.getLong(KAKAO_ACCESS_EXPIRATION, 0L)
    )

    fun getServerAuthData() = ServerAuthData(
        accessToken = sharedPreferences.getString(SERVER_ACCESS_TOKEN, "") ?: "",
        refreshToken = sharedPreferences.getString(SERVER_REFRESH_TOKEN, "") ?: ""
    )

    fun removeKakaoAuthData() {
        sharedPreferences.edit {
            remove(KAKAO_ACCESS_TOKEN)
            remove(KAKAO_REFRESH_TOKEN)
            remove(KAKAO_ACCESS_EXPIRATION)
        }
    }

    fun removeServerAuthData() {
        sharedPreferences.edit {
            remove(SERVER_ACCESS_TOKEN)
            remove(SERVER_REFRESH_TOKEN)
        }
    }

    fun updateKakaoPreference(data: KakaoAuthData) {
        sharedPreferences.edit {
            putString(KAKAO_ACCESS_TOKEN, data.kakaoAccessToken)
            putString(KAKAO_REFRESH_TOKEN, data.kakaoRefreshToken)
            putLong(KAKAO_ACCESS_EXPIRATION, data.accessTokenExpirationTime)
        }
    }

    fun updateServerPreference(data: ServerAuthData) {
        sharedPreferences.edit {
            putString(SERVER_ACCESS_TOKEN, data.accessToken)
            putString(SERVER_REFRESH_TOKEN, data.refreshToken)
        }
    }

    fun getUuid(): String = sharedPreferences.getString(KAKAO_UUID, "") ?: ""

    fun removeUuid() {
        sharedPreferences.edit {
            remove(KAKAO_UUID)
        }
    }

    fun updateUuid(uuid: String) {
        sharedPreferences.edit {
            putString(KAKAO_UUID, uuid)
        }
    }

    companion object {
        private const val KAKAO_ACCESS_TOKEN = "kakao_access_token"
        private const val KAKAO_REFRESH_TOKEN = "kakao_refresh_token"
        private const val KAKAO_ACCESS_EXPIRATION = "kakao_access_token_expiration"
        private const val SERVER_ACCESS_TOKEN = "server_access_token"
        private const val SERVER_REFRESH_TOKEN = "server_refresh_token"
        private const val KAKAO_UUID = "kakao_uuid"
    }
}
