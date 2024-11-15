package com.kappzzang.jeongsan.data

sealed class InviteMessageUiState {
    data object Idle : InviteMessageUiState()
    data object Fail : InviteMessageUiState()
    data object Success : InviteMessageUiState()
}
