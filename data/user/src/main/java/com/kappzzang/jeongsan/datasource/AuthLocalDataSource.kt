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

    // TODO: 추후에 EncryptedSharedPreferences를 사용하여 동기적으로 처리하기
    fun getUuidFlow(): Flow<String> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[KAKAO_UUID] ?: ""
    }

    suspend fun removeUuid() {
        dataStore.edit { preferences ->
            preferences.remove(KAKAO_UUID)
        }
    }

    suspend fun updateUuid(uuid: String) {
        dataStore.edit { preferences ->
            preferences[KAKAO_UUID] = uuid
        }
    }

    companion object {
        const val KAKAO_ACCESS_TOKEN = "kakao_access_token"
        private const val KAKAO_REFRESH_TOKEN = "kakao_refresh_token"
        private const val KAKAO_ACCESS_EXPIRATION = "kakao_access_token_expiration"
        private const val SERVER_ACCESS_TOKEN = "server_access_token"
        private const val SERVER_REFRESH_TOKEN = "server_refresh_token"
    }
}
