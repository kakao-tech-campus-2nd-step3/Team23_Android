package com.kappzzang.jeongsan.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.datastore: DataStore<Preferences> by preferencesDataStore(
    name = UserPreferencesModule.PREFERENCE_NAME
)

@Module
class UserPreferencesModule {
    @Binds


    companion object {
        const val PREFERENCE_NAME = "JeongsanPreference"
    }
}