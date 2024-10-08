package com.kappzzang.jeongsan.repositoryimpl

import android.content.Context
import com.kappzzang.jeongsan.data.AuthData
import com.kappzzang.jeongsan.datasource.AuthLocalDataSource
import com.kappzzang.jeongsan.util.AuthenticationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class AuthenticationRepositoryImpl
@Inject constructor(
    @ApplicationContext private val context: Context,
    datasource: AuthLocalDataSource
) : AuthenticationRepository {


    override suspend fun getAuthData(): AuthData {
        TODO("Not yet implemented")
    }

    override suspend fun updateAuthData(newData: AuthData) {
        TODO("Not yet implemented")
    }

    override suspend fun checkHasAuthData(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun removeAuthData() {
        TODO("Not yet implemented")
    }
}