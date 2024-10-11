package com.kappzzang.jeongsan.expenselist.sendmessage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.model.TransferDetailItem
import com.kappzzang.jeongsan.usecase.GetTransferInfoUseCase
import com.kappzzang.jeongsan.usecase.SendTransferMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SendMessageViewModel @Inject constructor(
    private val getTransferInfoUseCase: GetTransferInfoUseCase,
    private val sendTransferMessageUseCase: SendTransferMessageUseCase
) : ViewModel() {

    private val _transferInfo = MutableStateFlow<List<TransferDetailItem>>(emptyList())
    val transferInfo: StateFlow<List<TransferDetailItem>> = _transferInfo

    private val _totalPrice = MutableStateFlow(0)
    val totalPrice: StateFlow<Int> = _totalPrice

    init {
        loadTransferInfo()
    }

    private fun loadTransferInfo() {
        viewModelScope.launch {
            _transferInfo.value = getTransferInfoUseCase()
            calculateTotalPrice()
        }
    }

    private fun calculateTotalPrice() {
        _totalPrice.value = _transferInfo.value.sumOf { it.fee }
    }

    suspend fun sendTransferMessage(): Boolean = sendTransferMessageUseCase(_transferInfo.value)
}
