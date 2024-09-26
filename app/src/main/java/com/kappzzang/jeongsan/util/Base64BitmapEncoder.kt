package com.kappzzang.jeongsan.util

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.util.Base64


object Base64BitmapEncoder {
    fun convertBitmapToBase64String(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()
        val encoded = Base64.getEncoder().encodeToString(imageBytes)

        return encoded
    }
}
