package com.kappzzang.jeongsan.sendmessage

import android.content.Context
import com.kappzzang.jeongsan.sendmessage.data.HasTransferInfo
import com.kappzzang.jeongsan.sendmessage.data.TransferInfoUIState

object UiStateConverter {
    @JvmStatic
    fun getButtonEnabledFromState(state: TransferInfoUIState): Boolean =
        state is TransferInfoUIState.TransferInfoGetSuccess

    @JvmStatic
    fun getSumTextFromState(state: TransferInfoUIState, context: Context): String =
        if (state is HasTransferInfo) {
            "${(state.totalExpenseToGet)} ${context.getString(R.string.send_message_money_unit)}"
        } else {
            ""
        }
}
