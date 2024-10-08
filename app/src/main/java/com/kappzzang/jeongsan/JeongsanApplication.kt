package com.kappzzang.jeongsan

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JeongsanApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)

        printHashKey()
    }

    private fun printHashKey() {
        Log.d("KSC", KakaoSdk.keyHash)
    }
}
