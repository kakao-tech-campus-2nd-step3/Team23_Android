package com.kappzzang.jeongsan.sendmessage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.model.TransferDetailItem
import com.kappzzang.jeongsan.retrofit.error.InvalidInputError
import com.kappzzang.jeongsan.sendmessage.data.TransferInfoUIState
import com.kappzzang.jeongsan.usecase.GetPurchasedExpenseListUseCase
import com.kappzzang.jeongsan.usecase.GetTransferInfoUseCase
import com.kappzzang.jeongsan.usecase.SendTransferMessageUseCase
import com.kappzzang.jeongsan.usecase.SetExpensesToCompleteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SendMessageViewModel @Inject constructor(
    private val getTransferInfoUseCase: GetTransferInfoUseCase,
    private val sendTransferMessageUseCase: SendTransferMessageUseCase,
    private val getPurchasedExpenseListUseCase: GetPurchasedExpenseListUseCase,
    private val setExpensesToCompleteUseCase: SetExpensesToCompleteUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _groupId = MutableStateFlow("")
    val groupId: StateFlow<String>
        get() = _groupId
    private val _transferInfoState: MutableStateFlow<TransferInfoUIState> by lazy {
        initTransferInfoState()
    }
    val transferInfoState = _transferInfoState.asStateFlow()

    private fun initTransferInfoState(): MutableStateFlow<TransferInfoUIState> {
        val state = MutableStateFlow<TransferInfoUIState>(TransferInfoUIState.Idle)

        return state
    }

    fun startFetchUiState() {
        if (_transferInfoState.value != TransferInfoUIState.Idle) {
            return
        }

        viewModelScope.launch(ioDispatcher) {
            getPurchaseList()
        }
    }

    private suspend fun getPurchaseList() {
        _transferInfoState.value = TransferInfoUIState.LoadingPurchaseList
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
                getTransferInfo()
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

    private suspend fun getTransferInfo() {
        (_transferInfoState.value as? TransferInfoUIState.PurchaseListGetSuccess)?.let {
            _transferInfoState.value = TransferInfoUIState.LoadingTransferInfo(
                it.expenseIdList
            )
            launchGetStartInfoUseCase(it.expenseIdList)
        }
    }

    private suspend fun launchGetStartInfoUseCase(expenseIdList: List<String>) {
        getTransferInfoUseCase(
            groupId = groupId.value,
            expenseIdList = expenseIdList
        ).onSuccess {
            _transferInfoState.emit(
                TransferInfoUIState.TransferInfoGetSuccess(
                    transferInfoList = it,
                    totalExpenseToGet = it.sumOf { item -> item.fee },
                    expenseIdList = expenseIdList
                )
            )
        }.onFailure {
            if(it is InvalidInputError){
                _transferInfoState.emit(
                    TransferInfoUIState.TransferInfoGetError(
                        "송금 받을 지출이 존재하지 않습니다."
                    )
                )

            }
            else {
                _transferInfoState.emit(
                    TransferInfoUIState.TransferInfoGetError(
                        "결제 받을 목록을 불러오는 데 실패했습니다.\n${it.message}"
                    )
                )
            }
        }
    }

    fun sendTransferMessage() {
        (transferInfoState.value as? TransferInfoUIState.TransferInfoGetSuccess)?.let {
            launchSendTransferMessageUseCase(it.transferInfoList)
            _transferInfoState.value =
                TransferInfoUIState.SendingTransferMessage(
                    it.transferInfoList,
                    it.totalExpenseToGet,
                    it.expenseIdList
                )
        }
    }

    private fun launchSendTransferMessageUseCase(transferInfo: List<TransferDetailItem>) {
        viewModelScope.launch {
            sendTransferMessageUseCase(
                transferInfoList = transferInfo
            ).onSuccess {
                (transferInfoState.value as? TransferInfoUIState.SendingTransferMessage)?.let {
                    _transferInfoState.value =
                        TransferInfoUIState.TransferMessageSendSuccess(
                            it.transferInfoList,
                            it.totalExpenseToGet,
                            it.expenseIdList
                        )
                    updateToCompleted()
                }
            }.onFailure {
                _transferInfoState.value = TransferInfoUIState.TransferMessageSendError(
                    "송금 요청 메시지 전송을 실패했습니다: ${it.message}"
                )
            }
        }
    }

    private suspend fun updateToCompleted() {
        (transferInfoState.value as? TransferInfoUIState.TransferMessageSendSuccess)?.let {
            setExpensesToCompleteUseCase(
                groupId = groupId.value,
                expenseIdList = it.expenseIdList
            )
                .onSuccess {
                    _transferInfoState.emit(
                        TransferInfoUIState.ExpenseStateUpdateSuccess
                    )
                }.onFailure {
                    _transferInfoState.emit(
                        TransferInfoUIState.ExpenseStateUpdateError(
                            "지출 상태 변경에 실패했습니다: ${it.message}"
                        )
                    )
                }
        }
    }

    fun setGroupId(groupId: String?) {
        _groupId.value = groupId ?: ""
    }
}
