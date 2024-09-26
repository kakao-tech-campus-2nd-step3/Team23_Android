package com.kappzzang.jeongsan.ui.camera

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kappzzang.jeongsan.R
import com.kappzzang.jeongsan.databinding.ActivityReceiptCameraBinding

class ReceiptCameraActivity : AppCompatActivity() {
    private val binding: ActivityReceiptCameraBinding by lazy {
        ActivityReceiptCameraBinding.inflate(layoutInflater)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.receiptCameraViewFinder.surfaceProvider)

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                val camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview)

                val cameraController = camera.cameraControl
                cameraController.setZoomRatio(1F)
            } catch (e: Exception) {

            }

            preview.setSurfaceProvider(binding.receiptCameraViewFinder.surfaceProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(binding.root)
        startCamera()
    }
}
