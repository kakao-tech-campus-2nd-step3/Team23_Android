package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.repository.KakaoAuthenticationRepository
import com.kappzzang.jeongsan.repository.UserInfoRepository
import com.kappzzang.jeongsan.usecase.AuthenticateWithKakaoUseCase
import com.kappzzang.jeongsan.usecase.AuthorizeWithKakaoUseCase
import com.kappzzang.jeongsan.usecase.GetUserInfoUseCase
import com.kappzzang.jeongsan.usecase.RegisterWithKakaoUseCase
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
    fun provideAuthorizeWithKakaoUseCase(authenticationRepository: AuthenticationRepository) =
        AuthorizeWithKakaoUseCase(authenticationRepository)

    @Provides
    fun provideGetUserInfoUseCase(userInfoRepository: UserInfoRepository) =
        GetUserInfoUseCase(userInfoRepository)

    @Provides
    fun registerWithKakaoUseCase() = RegisterWithKakaoUseCase()
}
