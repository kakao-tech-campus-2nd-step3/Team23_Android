package com.kappzzang.jeongsan.ui.camera

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReceiptCameraViewModel : ViewModel() {
    enum class ReceiptPictureState { NOT_TAKEN, READY_TO_SEND, SENDING_TO_SERVER, RECEIVE_SERVER_RESPONSE }

    private val _receiptPictureState = MutableStateFlow(ReceiptPictureState.NOT_TAKEN)
    private val _pictureData = MutableStateFlow<Uri>(Uri.EMPTY)

    val receiptPictureState = _receiptPictureState.asStateFlow()
    val pictureData = _pictureData.asStateFlow()

    fun setPictureData(pictureUri: Uri){
        _pictureData.value = pictureUri
        _receiptPictureState.value = ReceiptPictureState.READY_TO_SEND
        Log.d("KSC", "Saved Picture")
    }
}
