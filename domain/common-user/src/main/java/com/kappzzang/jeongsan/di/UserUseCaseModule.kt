package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.repository.KakaoAuthenticationRepository
import com.kappzzang.jeongsan.repository.ServerAuthenticationRepository
import com.kappzzang.jeongsan.repository.UserInfoRepository
import com.kappzzang.jeongsan.usecase.AuthenticateWithKakaoUseCase
import com.kappzzang.jeongsan.usecase.AuthorizeWithKakaoUseCase
import com.kappzzang.jeongsan.usecase.GetUserInfoUseCase
import com.kappzzang.jeongsan.usecase.GetUserServiceIdUseCase
import com.kappzzang.jeongsan.usecase.LoginOrRegisterUseCase
import com.kappzzang.jeongsan.util.AuthenticationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserUseCaseModule {

    @Provides
    fun provideAuthenticateWithKakaoUseCase(
        authenticationRepository: AuthenticationRepository,
        kakaoAuthenticationRepository: KakaoAuthenticationRepository
    ) = AuthenticateWithKakaoUseCase(authenticationRepository, kakaoAuthenticationRepository)

    @Provides
    fun provideAuthenticateWithServerUseCase(
        authenticationRepository: AuthenticationRepository,
        serverAuthenticationRepository: ServerAuthenticationRepository
    ) = LoginOrRegisterUseCase(authenticationRepository, serverAuthenticationRepository)

    @Provides
    fun provideAuthorizeWithKakaoUseCase(authenticationRepository: AuthenticationRepository) =
        AuthorizeWithKakaoUseCase(authenticationRepository)

    @Provides
    fun provideGetUserInfoUseCase(userInfoRepository: UserInfoRepository) =
        GetUserInfoUseCase(userInfoRepository)

    @Provides
    fun provideGetUserServiceIdUseCase(authenticationRepository: AuthenticationRepository) =
        GetUserServiceIdUseCase(authenticationRepository)
}
