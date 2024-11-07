package com.kappzzang.jeongsan.retrofit

import com.kappzzang.jeongsan.build_config.BuildConfig
import com.kappzzang.jeongsan.util.AuthenticationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KakaoAuthRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KakaoApiRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ServiceRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ServiceAuthRetrofit

    @Provides
    @Singleton
    @KakaoAuthRetrofit
    fun provideKakaoAuthRetrofitBuilder(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.KAKAO_AUTH_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @KakaoApiRetrofit
    fun provideKakaoApiRetrofitBuilder(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.KAKAO_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @ServiceAuthRetrofit
    fun provideServiceAuthRetrofitBuilder(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.SERVICE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @ServiceRetrofit
    fun provideServiceRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.SERVICE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideHeaderInterceptor(authRepository: AuthenticationRepository) =
        HeaderInterceptor(authRepository)

    @Provides
    @Singleton
    fun provideAuthInterceptor(authRepository: AuthenticationRepository) =
        AuthInterceptor(authRepository)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        headerInterceptor: HeaderInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .authenticator(authInterceptor)
        .build()
}
