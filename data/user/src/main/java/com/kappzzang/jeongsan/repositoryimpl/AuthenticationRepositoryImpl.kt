package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.data.AuthData
import com.kappzzang.jeongsan.datasource.AuthLocalDataSource
import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class AuthenticationRepositoryImpl
@Inject constructor(
    private val datasource: AuthLocalDataSource
) : AuthenticationRepository {

    override fun getAuthData(): Flow<AuthData> = datasource.getAuthDataFlow()

    override suspend fun updateAuthData(newData: AuthData) {
        datasource.updatePreference(newData)
    }

    override suspend fun removeAuthData() {
        TODO("Not yet implemented")
    }
}
