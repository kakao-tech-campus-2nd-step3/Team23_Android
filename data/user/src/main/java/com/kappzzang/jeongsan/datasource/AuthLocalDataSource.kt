package com.kappzzang.jeongsan.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kappzzang.jeongsan.data.AuthData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class AuthLocalDataSource @Inject constructor(private val dataStore: DataStore<Preferences>) {

    fun getAuthDataFlow(): Flow<AuthData> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        AuthData(
            kakaoAccessToken = preferences[ACCESS_TOKEN] ?: "",
            kakaoRefreshToken = preferences[REFRESH_TOKEN] ?: "",
            accessTokenExpirationTime = preferences[ACCESS_EXPIRATION] ?: 0L,
            jwt = preferences[JWT]
        )
    }

    suspend fun updatePreference(data: AuthData) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = data.kakaoAccessToken
            preferences[REFRESH_TOKEN] = data.kakaoRefreshToken
            preferences[ACCESS_EXPIRATION] = data.accessTokenExpirationTime
            data.jwt?.let { preferences[JWT] = it }
        }
    }

    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("kakao_access_token")
        val REFRESH_TOKEN = stringPreferencesKey("kakao_refresh_token")
        val ACCESS_EXPIRATION = longPreferencesKey("kakao_access_token_expiration")
        val JWT = stringPreferencesKey("service_jwt")
    }
}
