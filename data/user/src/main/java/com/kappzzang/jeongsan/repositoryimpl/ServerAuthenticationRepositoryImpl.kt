package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.data.ServerAuthData
import com.kappzzang.jeongsan.datasource.ServerAuthRemoteDataSource
import com.kappzzang.jeongsan.repository.ServerAuthenticationRepository
import javax.inject.Inject

class ServerAuthenticationRepositoryImpl @Inject constructor(
    val dataSource: ServerAuthRemoteDataSource
) : ServerAuthenticationRepository {

    override fun loginToServer(email: String): ServerAuthData {
        TODO("Not yet implemented")
    }

    override fun registerToServer(
        nickname: String,
        email: String,
        profileImageUrl: String
    ): ServerAuthData {
        TODO("Not yet implemented")
    }

    override fun refreshJwtFromServer(authData: ServerAuthData): ServerAuthData {
        TODO("Not yet implemented")
    }
}
