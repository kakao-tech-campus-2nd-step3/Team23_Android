package com.kappzzang.jeongsan.datasource

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
import javax.inject.Inject

internal class AuthLocalDataSource @Inject constructor(@ApplicationContext private val context: Context) {
    private fun getKeyStore() : KeyStore =
        KeyStore.getInstance("JeongSanKeyStore")

    fun checkContainsToken() : Boolean {
        val keyStore = getKeyStore()
        keyStore.load(null)
    }

    companion object {
        const val KEYSTORE_VERSION = 1
    }
}