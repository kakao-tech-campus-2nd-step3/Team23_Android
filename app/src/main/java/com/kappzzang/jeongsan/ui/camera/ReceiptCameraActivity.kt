package com.kappzzang.jeongsan.ui.camera

import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.kappzzang.jeongsan.R
import com.kappzzang.jeongsan.databinding.ActivityReceiptCameraBinding
import com.kappzzang.jeongsan.domain.model.OcrResultResponse
import com.kappzzang.jeongsan.ui.expenselist.ExpenseListActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class ReceiptCameraActivity : AppCompatActivity() {
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

            }

            preview.setSurfaceProvider(binding.receiptCameraViewFinder.surfaceProvider)
        }, ContextCompat.getMainExecutor(this))
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

    private fun setShutterButtonListener() {
        binding.receiptCameraCaptureButton.setOnClickListener {
            photoFile.delete()
            takePicture()
        }
    }

    private fun getOcrResultIntent(response: OcrResultResponse): Intent{
        return Intent(applicationContext, ExpenseListActivity::class.java).apply {
            putExtra(OCR_RESULT ,response)
        }
    }

    private fun collectReceiptPictureState(state: ReceiptCameraViewModel.ReceiptPictureState){
        when (state) {
            ReceiptCameraViewModel.ReceiptPictureState.NOT_TAKEN -> {
                startCamera()
            }

            ReceiptCameraViewModel.ReceiptPictureState.READY_TO_SEND -> {
                viewModel.serverResponse?.let {
                    setResult(RESULT_OK, getOcrResultIntent(it))
                }
            }

            ReceiptCameraViewModel.ReceiptPictureState.SENDING_TO_SERVER -> {
                (binding.receiptCameraLoadingCircles.drawable as? AnimatedVectorDrawable)?.start()
            }

            ReceiptCameraViewModel.ReceiptPictureState.RECEIVE_SERVER_RESPONSE -> {
                Toast.makeText(this@ReceiptCameraActivity, "완료!", Toast.LENGTH_SHORT)
                    .show()

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
            }
        }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setShutterButtonListener()
        collectCameraPictureData()

        setContentView(binding.root)
    }

    companion object {
        const val OCR_RESULT = "ocr_result"
    }
}
