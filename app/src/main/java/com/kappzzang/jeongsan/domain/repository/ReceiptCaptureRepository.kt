package com.kappzzang.jeongsan.domain.repository

import java.io.File

interface ReceiptCaptureRepository {
    fun getImageCacheFile(): File
}
