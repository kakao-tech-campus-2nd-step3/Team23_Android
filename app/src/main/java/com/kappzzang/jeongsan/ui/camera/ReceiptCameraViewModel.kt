package com.kappzzang.jeongsan.ui.camera

import android.app.Application
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.domain.model.OcrResultResponse
import com.kappzzang.jeongsan.domain.usecase.AnalyzeReceiptImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ReceiptCameraViewModel @Inject constructor(
    private val application: Application,
    private val analyzeReceiptImageUseCase: AnalyzeReceiptImageUseCase
) : AndroidViewModel(application) {
    enum class ReceiptPictureState {
        NOT_TAKEN,
        READY_TO_SEND,
        SENDING_TO_SERVER,
        RECEIVE_SERVER_RESPONSE,
        ERROR
    }

    private val _receiptPictureState = MutableStateFlow(ReceiptPictureState.NOT_TAKEN)
    private val _pictureData = MutableStateFlow<Uri>(Uri.EMPTY)

    val receiptPictureState = _receiptPictureState.asStateFlow()
    val pictureData = _pictureData.asStateFlow()
    var serverResponse: OcrResultResponse? = null
        private set
    var serverErrorMessage: String? = null
        private set

    private fun sendPictureToServer(pictureUri: Uri) {
        val bitmap: Bitmap
        try {
            bitmap = ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    application.contentResolver,
                    pictureUri
                )
            )
        } catch (e: IOException) {
            _receiptPictureState.value = ReceiptPictureState.ERROR
            serverErrorMessage = e.message
            Log.e("KSC", e.message.toString())
            return
        }

        _receiptPictureState.value = ReceiptPictureState.SENDING_TO_SERVER

        viewModelScope.launch(Dispatchers.IO) {
            val response = analyzeReceiptImageUseCase(bitmap)

            when (response) {
                is OcrResultResponse.OcrFailed -> {
                    Log.e("KSC", response.message)
                    serverErrorMessage = response.message
                    _receiptPictureState.value = ReceiptPictureState.ERROR
                }
                is OcrResultResponse.OcrSuccess -> {
                    serverResponse = response
                    _receiptPictureState.value = ReceiptPictureState.RECEIVE_SERVER_RESPONSE
                }
            }
        }
    }

    fun setPictureData(pictureUri: Uri) {
        _pictureData.value = pictureUri
        _receiptPictureState.value = ReceiptPictureState.READY_TO_SEND
        sendPictureToServer(pictureUri)
        Log.d("KSC", "Saved Picture")
    }
}
