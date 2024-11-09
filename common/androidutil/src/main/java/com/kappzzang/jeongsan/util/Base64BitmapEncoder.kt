package com.kappzzang.jeongsan.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import java.util.Base64

object Base64BitmapEncoder {
    fun convertBitmapToBase64String(bitmap: Bitmap): String {
        val resized = resizeBitmap(bitmap, 1024)
        val byteArrayOutputStream = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()
        val encoded = Base64.getEncoder().encodeToString(imageBytes)

        return encoded
    }

    private fun calculateInSampleSize(width: Int, height: Int, sampleDimen: Int): Int {
        var inSampleSize = 1
        while (width / inSampleSize > sampleDimen || height / inSampleSize > sampleDimen) {
            inSampleSize++
        }
        return inSampleSize
    }

    private fun resizeBitmap(bitmap: Bitmap, sampleDimen: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val inSampleSize = (calculateInSampleSize(width, height, sampleDimen))
        if (inSampleSize == 1) {
            return bitmap
        }
        val sampleWidth = bitmap.width / inSampleSize
        val sampleHeight = bitmap.height / inSampleSize
        val result = Bitmap.createScaledBitmap(bitmap, sampleWidth, sampleHeight, true)
        return result
    }

    fun convertUriToBitmap(uri: Uri, context: Context): Bitmap = if (Build.VERSION.SDK_INT >= 28) {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    } else {
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    }
}
