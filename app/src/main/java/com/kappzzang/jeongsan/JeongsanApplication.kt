package com.kappzzang.jeongsan

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
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

    companion object {
        const val DATASTORE_NAME = "JeongsanDatastore"
    }
}
