package com.kappzzang.jeongsan.data

sealed class ExpenseDetailState {
    data object Idle: ExpenseDetailState()

    data object Uploading: ExpenseDetailState()

    data object SwitchingState: ExpenseDetailState()

    data object Success: ExpenseDetailState()

    data class Failed(val closeAfterCatch: Boolean, val message: String): ExpenseDetailState()
}
