package com.kappzzang.jeongsan.datasource

import android.content.Context
import com.kappzzang.jeongsan.user.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
import javax.inject.Inject

internal class AuthLocalDataSource @Inject constructor(@ApplicationContext private val context: Context) {
    private fun getKeyStore() : KeyStore =
        KeyStore.getInstance(BuildConfig.KEYSTORE_NAME)

    fun checkContainsToken() : Boolean {
        val keyStore = getKeyStore()
        keyStore.load(null)

        return false
    }

    companion object {
        const val KEYSTORE_VERSION = 1
    }
}