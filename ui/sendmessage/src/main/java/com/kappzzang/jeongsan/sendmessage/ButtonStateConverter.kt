package com.kappzzang.jeongsan.sendmessage

import com.kappzzang.jeongsan.sendmessage.data.TransferInfoUIState

object ButtonStateConverter {
    @JvmStatic
    fun getButtonEnabledFromState(state: TransferInfoUIState): Boolean {
        return state is TransferInfoUIState.TransferInfoGetError
    }
}
