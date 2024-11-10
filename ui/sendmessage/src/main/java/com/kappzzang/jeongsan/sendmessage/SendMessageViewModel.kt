package com.kappzzang.jeongsan.sendmessage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.model.TransferDetailItem
import com.kappzzang.jeongsan.sendmessage.data.TransferInfoUIState
import com.kappzzang.jeongsan.usecase.GetPurchasedExpenseListUseCase
import com.kappzzang.jeongsan.usecase.GetTransferInfoUseCase
import com.kappzzang.jeongsan.usecase.SendTransferMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SendMessageViewModel @Inject constructor(
    private val getTransferInfoUseCase: GetTransferInfoUseCase,
    private val sendTransferMessageUseCase: SendTransferMessageUseCase,
    private val getPurchasedExpenseListUseCase: GetPurchasedExpenseListUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _groupId = MutableStateFlow("")
    val groupId: StateFlow<String>
        get() = _groupId
    private val _transferInfoState: MutableStateFlow<TransferInfoUIState> by lazy { initTransferInfoState() }
    val transferInfoState = _transferInfoState.asStateFlow()

    private fun initTransferInfoState(): MutableStateFlow<TransferInfoUIState> {
        val state = MutableStateFlow<TransferInfoUIState>(TransferInfoUIState.Idle)

        return state
    }

    fun getPurchasedExpenseList() {
        if (_transferInfoState.value != TransferInfoUIState.Idle) {
            return
        }
        _transferInfoState.value = TransferInfoUIState.LoadingPurchaseList
        viewModelScope.launch(ioDispatcher) {
            getPurchasedExpenseListUseCase(_groupId.value)
                .onSuccess {
                    _transferInfoState.emit(
                        TransferInfoUIState.PurchaseListGetSuccess(
                            it.size,
                            it.sumOf { item ->
                                item.price
                            },
                            it.map { item ->
                                item.id
                            }
                        )
                    )
                }
                .onFailure {
                    it.printStackTrace()
                    _transferInfoState.emit(
                        TransferInfoUIState.PurchaseListGetError(
                            "결제한 지출 정보를 불러오는 데 실패했습니다.\n${it.message}"
                        )
                    )
                }
        }
    }

    fun getTransferInfo() {
        val itemToCalculate: List<String>
        if (_transferInfoState.value !is TransferInfoUIState.PurchaseListGetSuccess) {
            return
        } else {
            itemToCalculate =
                (_transferInfoState.value as TransferInfoUIState.PurchaseListGetSuccess).expenseIdList
        }
        _transferInfoState.value = TransferInfoUIState.LoadingTransferInfo
        viewModelScope.launch(ioDispatcher) {
            delay(1000)
            getTransferInfoUseCase(
                groupId = groupId.value,
                expenseIdList = emptyList()
            ).onSuccess {
                _transferInfoState.emit(
                    TransferInfoUIState.TransferInfoGetSuccess(
                        transferInfoList = it,
                        totalExpenseToGet = it.sumOf { item -> item.fee }
                    )
                )

            }.onFailure {
                it.printStackTrace()
                _transferInfoState.emit(
                    TransferInfoUIState.TransferInfoGetError(
                        "결제 받을 목록을 불러오는 데 실패했습니다.\n${it.message}"
                    )
                )
            }
        }
    }

    private fun loadTransferInfo(expenseList: List<String>) {
        viewModelScope.launch(ioDispatcher) {
            getTransferInfoUseCase(
                groupId = groupId.value,
                expenseIdList = expenseList
            )
        }
    }

    //suspend fun sendTransferMessage(): Boolean = sendTransferMessageUseCase(_transferInfo.value)

    fun setGroupId(groupId: String?) {
        _groupId.value = groupId ?: ""
    }
}
