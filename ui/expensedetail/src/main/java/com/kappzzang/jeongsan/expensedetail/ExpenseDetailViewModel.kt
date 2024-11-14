package com.kappzzang.jeongsan.expensedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.data.ExpenseDetailState
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.retrofit.error.AuthenticateError
import com.kappzzang.jeongsan.retrofit.error.InvalidInputError
import com.kappzzang.jeongsan.usecase.RevertExpenseToOngoingUseCase
import com.kappzzang.jeongsan.usecase.SetExpenseToPendingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class ExpenseDetailPage { EXPENSE_DETAIL, SELECTION_STATUS }

internal fun ExpenseState.editable(): Boolean =
    this == ExpenseState.NOT_CONFIRMED || this == ExpenseState.CONFIRMED

@HiltViewModel
class ExpenseDetailViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val setExpenseToPendingUseCase: SetExpenseToPendingUseCase,
    private val setExpenseToOngoingUseCase: RevertExpenseToOngoingUseCase
) : ViewModel() {
    private val _currentPage = MutableStateFlow(ExpenseDetailPage.EXPENSE_DETAIL)
    private val _showPayerUI = MutableStateFlow(false)
    private val _isPayer = MutableStateFlow(false)

    private val _expenseDetailState = MutableStateFlow<ExpenseDetailState>(ExpenseDetailState.Idle)

    var groupId = ""
        private set
    var expenseId = ""
        private set
    private val _expenseState = MutableStateFlow(ExpenseState.TRANSFERED)
    val expenseState = _expenseState.asStateFlow()

    val currentPage = _currentPage.asStateFlow()
    val showPayerUI = _showPayerUI.asStateFlow()
    val expenseDetailState = _expenseDetailState.asStateFlow()
    val isPayer = _isPayer.asStateFlow()

    private fun switchToPendingExpense() {
        _expenseDetailState.value = ExpenseDetailState.SwitchingState

        viewModelScope.launch(ioDispatcher) {
            setExpenseToPendingUseCase.invoke(expenseId, groupId)
                .onSuccess {
                    _expenseDetailState.value = ExpenseDetailState.Success
                }
                .onFailure {
                    when (it) {
                        is AuthenticateError -> {
                            _expenseDetailState.value = ExpenseDetailState.Failed(
                                true,
                                "인증 오류가 발생했습니다. 다시 로그인 해주세요"
                            )
                        }

                        is InvalidInputError -> {
                            _expenseDetailState.value = ExpenseDetailState.Failed(
                                false,
                                "멤버들의 선택 항목이 결제 수량보다 같거나 커야 합니다."
                            )
                        }

                        else -> {
                            _expenseDetailState.value = ExpenseDetailState.Failed(
                                true,
                                "알 수 없는 오류 발생: ${it.message}"
                            )
                        }
                    }
                }
        }
    }

    private fun switchToOngoingExpense() {
        _expenseDetailState.value = ExpenseDetailState.SwitchingState

        viewModelScope.launch(ioDispatcher) {
            setExpenseToOngoingUseCase.invoke(expenseId, groupId)
                .onSuccess {
                    _expenseDetailState.value = ExpenseDetailState.Success
                }
                .onFailure {
                    when (it) {
                        is AuthenticateError -> {
                            _expenseDetailState.value = ExpenseDetailState.Failed(
                                true,
                                "인증 오류가 발생했습니다. 다시 로그인 해주세요"
                            )
                        }

                        is InvalidInputError -> {
                            _expenseDetailState.value = ExpenseDetailState.Failed(
                                false,
                                "멤버들의 선택 항목이 결제 수량보다 같거나 커야 합니다."
                            )
                        }

                        else -> {
                            _expenseDetailState.value = ExpenseDetailState.Failed(
                                true,
                                "알 수 없는 오류 발생: ${it.message}"
                            )
                        }
                    }
                }
        }
    }

    fun revertStateToIdle() {
        _expenseDetailState.value = ExpenseDetailState.Idle
    }

    fun setSaveResult(isSuccess: Boolean) {
        _expenseDetailState.value =
            if (isSuccess) {
                ExpenseDetailState.Success
            } else {
                ExpenseDetailState.Failed(true, "지출 등록에 실패했습니다.")
            }
    }

    fun setInitialData(
        expenseId: String,
        groupId: String,
        expenseState: ExpenseState,
        isPayer: Boolean
    ) {
        if (this.groupId.isNotEmpty()) {
            return
        }
        this.expenseId = expenseId
        this.groupId = groupId
        this._isPayer.value = isPayer
        _expenseState.value = expenseState

        if (isPayer) {
            _showPayerUI.value = true
            _currentPage.value = ExpenseDetailPage.SELECTION_STATUS
        } else {
            _showPayerUI.value = false
            _currentPage.value = ExpenseDetailPage.EXPENSE_DETAIL
        }
    }

    private fun dismissAndClose() {
        _expenseDetailState.value = ExpenseDetailState.Success
    }

    private fun saveDetailsAndClose() {
        _expenseDetailState.value = ExpenseDetailState.Uploading
    }

    private fun navigateToSelectionStatus() {
        _currentPage.value = ExpenseDetailPage.SELECTION_STATUS
    }

    private fun navigateToExpenseDetail() {
        _currentPage.value = ExpenseDetailPage.EXPENSE_DETAIL
    }

    private fun clickDetailPageLeftButton() {
        when (expenseState.value) {
            ExpenseState.CONFIRMED, ExpenseState.NOT_CONFIRMED -> saveDetailsAndClose()
            ExpenseState.TRANSFER_PENDING -> dismissAndClose()
            ExpenseState.TRANSFERED -> dismissAndClose()
        }
    }

    private fun clickStatusPageLeftButton() {
        when (expenseState.value) {
            ExpenseState.CONFIRMED, ExpenseState.NOT_CONFIRMED -> {
                if (_isPayer.value) {
                    switchToPendingExpense()
                } else {
                    dismissAndClose()
                }
            }

            ExpenseState.TRANSFER_PENDING -> {
                if (_isPayer.value) {
                    switchToOngoingExpense()
                } else {
                    dismissAndClose()
                }
            }

            ExpenseState.TRANSFERED -> dismissAndClose()
        }
    }

    private fun clickDetailPageRightButton() {
        navigateToSelectionStatus()
    }

    private fun clickStatusPageRightButton() {
        navigateToExpenseDetail()
    }

    fun clickLeftButton() {
        if (currentPage.value == ExpenseDetailPage.EXPENSE_DETAIL) {
            clickDetailPageLeftButton()
        } else {
            clickStatusPageLeftButton()
        }
    }

    fun clickRightButton() {
        if (currentPage.value == ExpenseDetailPage.EXPENSE_DETAIL) {
            clickDetailPageRightButton()
        } else {
            clickStatusPageRightButton()
        }
    }
}
