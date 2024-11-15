package com.kappzzang.jeongsan.kakaoclient

import com.kakao.sdk.talk.TalkApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KakaoClientModule {
    @Provides
    @Singleton
    fun provideKakaoClient(): TalkApiClient = TalkApiClient.instance
}
