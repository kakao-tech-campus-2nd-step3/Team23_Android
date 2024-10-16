package com.kappzzang.jeongsan.camera

import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.kappzzang.jeongsan.camera.databinding.ActivityReceiptCameraBinding
import com.kappzzang.jeongsan.intentcontract.ReceiptCameraContract
import com.kappzzang.jeongsan.model.OcrResultResponse
import com.kappzzang.jeongsan.navigation.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReceiptCameraActivity : AppCompatActivity() {
    @Inject
    lateinit var appNavigator: AppNavigator
    private val binding: ActivityReceiptCameraBinding by lazy {
        ActivityReceiptCameraBinding.inflate(layoutInflater)
    }
    private val viewModel: ReceiptCameraViewModel by viewModels()

    private var imageCapture: ImageCapture? = null

    private val photoFile by lazy {
        File(
            applicationContext.cacheDir,
            "receipt.jpg"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setShutterButtonListener()
        collectCameraPictureData()

        setContentView(binding.root)
    }

    private fun setShutterButtonListener() {
        binding.receiptCameraCaptureButton.setOnClickListener {
            photoFile.delete()
            takePicture()
        }
    }

    private fun takePicture() {
        val mImageCapture = imageCapture ?: return
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        mImageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    outputFileResults.savedUri?.let { uri ->
                        viewModel.setPictureData(uri)
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        applicationContext,
                        resources.getString(R.string.receipt_camera_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    private fun collectCameraPictureData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pictureData.collect {
                    if (it == Uri.EMPTY) {
                        return@collect
                    }

                    Glide.with(this@ReceiptCameraActivity)
                        .load(it)
                        .apply(
                            RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .centerInside()
                        )
                        .into(binding.receiptCameraImageImageview)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.receiptPictureState.collect {
                    collectReceiptPictureState(state = it)
                }
            }
        }
    }

    private fun collectReceiptPictureState(state: ReceiptCameraViewModel.ReceiptPictureState) {
        when (state) {
            ReceiptCameraViewModel.ReceiptPictureState.NOT_TAKEN -> {
                startCamera()
            }

            ReceiptCameraViewModel.ReceiptPictureState.READY_TO_SEND -> {
                // 현재 사용하지 않는 상태
            }

            ReceiptCameraViewModel.ReceiptPictureState.SENDING_TO_SERVER -> {
                (binding.receiptCameraLoadingCircles.drawable as? AnimatedVectorDrawable)?.start()
            }

            ReceiptCameraViewModel.ReceiptPictureState.RECEIVE_SERVER_RESPONSE -> {
                viewModel.serverResponse?.let {
                    setResult(RESULT_OK, getOcrResultIntent(it))
                }

                finish()
            }

            ReceiptCameraViewModel.ReceiptPictureState.ERROR -> {
                Toast.makeText(
                    this@ReceiptCameraActivity,
                    viewModel.serverErrorMessage,
                    Toast.LENGTH_SHORT
                ).show()

                viewModel.serverResponse?.let {
                    setResult(RESULT_CANCELED, getOcrResultIntent(it))
                }

                finish()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.receiptCameraViewFinder.surfaceProvider)

            imageCapture = ImageCapture.Builder()
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                val camera =
                    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

                val cameraController = camera.cameraControl
                cameraController.setZoomRatio(1F)
            } catch (e: Exception) {
                Toast.makeText(
                    applicationContext,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()

                val intent = appNavigator.navigateToExpenseList(applicationContext)
                setResult(RESULT_CANCELED, intent)
                finish()
            }

            preview.setSurfaceProvider(binding.receiptCameraViewFinder.surfaceProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun getOcrResultIntent(response: OcrResultResponse): Intent =
        appNavigator.navigateToExpenseList(applicationContext).apply {
            putExtra(ReceiptCameraContract.OCR_RESULT, response)
            putExtra(ReceiptCameraContract.OCR_RESULT_IMAGE, viewModel.pictureData.value)
        }
}
