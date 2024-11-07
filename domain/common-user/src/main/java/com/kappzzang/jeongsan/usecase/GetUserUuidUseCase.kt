package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class GetUserUuidUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {

    // TODO: 추후에 EncryptedSharedPreferences를 사용하여 동기적으로 처리하기
    operator fun invoke(): String = runBlocking {
        authenticationRepository.getUuid().first()
    }
}
