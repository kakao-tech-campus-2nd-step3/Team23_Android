package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.repository.LoginRepository

internal class LoginRepositoryImpl: LoginRepository {
    override suspend fun getJwtFromServer(accessToken: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun registerToServer(accessToken: String): String {
        TODO("Not yet implemented")
    }
}