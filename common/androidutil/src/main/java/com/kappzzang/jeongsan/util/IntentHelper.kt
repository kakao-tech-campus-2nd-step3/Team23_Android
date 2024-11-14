package com.kappzzang.jeongsan.util

import android.content.Intent
import android.os.Build

object IntentHelper {
    inline fun <reified T> Intent.getParcelableData(key: String): T? {
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelableExtra(
                key,
                T::class.java
            )
        } else {
            (getParcelableExtra(key) as? T) ?: let{
                getSerializableExtra(key) as? T
            }
        }
        return data
    }
}
