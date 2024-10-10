package com.kappzzang.jeongsan.repositoryimpl

import android.content.Context
import com.kappzzang.jeongsan.data.AuthData
import com.kappzzang.jeongsan.datasource.AuthLocalDataSource
import com.kappzzang.jeongsan.util.AuthenticationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthenticationRepositoryImpl
@Inject constructor(
    private val datasource: AuthLocalDataSource
) : AuthenticationRepository {

    override suspend fun getAuthData(): Flow<AuthData> =
        datasource.getAuthDataFlow()

    override suspend fun updateAuthData(newData: AuthData) {
        datasource.updatePreference(newData)
    }

    override suspend fun removeAuthData() {
        TODO("Not yet implemented")
    }
}