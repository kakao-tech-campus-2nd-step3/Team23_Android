package com.kappzzang.jeongsan

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.kakao.sdk.common.KakaoSdk
import com.kappzzang.jeongsan.build_config.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JeongsanApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)

        // 다크 모드인 경우에도 라이트 모드로 설정하여 의도한 UI를 보여줄 수 있도록 함
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        printHashKey()
    }

    private fun printHashKey() {
        Log.d("KSC", KakaoSdk.keyHash)
    }

    companion object {
        const val ENCRYPTED_SHARED_PREFERENCES_NAME = "JeongsanEncryptedSharedPreferences"
    }
}
