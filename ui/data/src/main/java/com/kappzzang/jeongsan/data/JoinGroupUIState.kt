package com.kappzzang.jeongsan.data

sealed class JoinGroupUIState {
    data object Idle : JoinGroupUIState()
    data class Success(val groupId: String) : JoinGroupUIState()
    data class Error(val message: String) : JoinGroupUIState()
}
