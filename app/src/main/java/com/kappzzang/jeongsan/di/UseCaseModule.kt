package com.kappzzang.jeongsan.di

import com.kappzzang.jeongsan.domain.repository.MemberRepository
import com.kappzzang.jeongsan.domain.usecase.GetInviteInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetInviteInfoUseCase(
        memberRepository: MemberRepository
    ): GetInviteInfoUseCase = GetInviteInfoUseCase(memberRepository)
}
