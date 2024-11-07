package com.kappzzang.jeongsan.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kappzzang.jeongsan.data.KakaoAuthData
import com.kappzzang.jeongsan.data.ServerAuthData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class AuthLocalDataSource @Inject constructor(private val dataStore: DataStore<Preferences>) {

    fun getKakaoAuthDataFlow(): Flow<KakaoAuthData> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        KakaoAuthData(
            kakaoAccessToken = preferences[KAKAO_ACCESS_TOKEN] ?: "",
            kakaoRefreshToken = preferences[KAKAO_REFRESH_TOKEN] ?: "",
            accessTokenExpirationTime = preferences[KAKAO_ACCESS_EXPIRATION] ?: 0L
        )
    }

    fun getServerAuthDataFlow(): Flow<ServerAuthData> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        ServerAuthData(
            accessToken = preferences[SERVER_ACCESS_TOKEN] ?: "",
            refreshToken = preferences[SERVER_REFRESH_TOKEN] ?: ""
        )
    }

    suspend fun removeKakaoAuthData() {
        dataStore.edit { preferences ->
            preferences.remove(KAKAO_ACCESS_TOKEN)
            preferences.remove(KAKAO_REFRESH_TOKEN)
            preferences.remove(KAKAO_ACCESS_EXPIRATION)
        }
    }

    suspend fun removeServerAuthData() {
        dataStore.edit { preferences ->
            preferences.remove(SERVER_ACCESS_TOKEN)
            preferences.remove(SERVER_REFRESH_TOKEN)
        }
    }

    suspend fun updateKakaoPreference(data: KakaoAuthData) {
        dataStore.edit { preferences ->
            preferences[KAKAO_ACCESS_TOKEN] = data.kakaoAccessToken
            preferences[KAKAO_REFRESH_TOKEN] = data.kakaoRefreshToken
            preferences[KAKAO_ACCESS_EXPIRATION] = data.accessTokenExpirationTime
        }
    }

    suspend fun updateServerPreference(data: ServerAuthData) {
        dataStore.edit { preferences ->
            preferences[SERVER_ACCESS_TOKEN] = data.accessToken
            preferences[SERVER_REFRESH_TOKEN] = data.refreshToken
        }
    }

    companion object {
        val KAKAO_ACCESS_TOKEN = stringPreferencesKey("kakao_access_token")
        val KAKAO_REFRESH_TOKEN = stringPreferencesKey("kakao_refresh_token")
        val KAKAO_ACCESS_EXPIRATION = longPreferencesKey("kakao_access_token_expiration")
        val SERVER_ACCESS_TOKEN = stringPreferencesKey("server_access_token")
        val SERVER_REFRESH_TOKEN = stringPreferencesKey("server_refresh_token")
    }
}
