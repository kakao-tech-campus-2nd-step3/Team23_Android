package com.kappzzang.jeongsan.expenselist.util

import com.kappzzang.jeongsan.data.ExpenseListUIState
import com.kappzzang.jeongsan.data.HasGroupInfo

object ExpenseStateConverter {
    @JvmStatic
    fun convertExpenseUIStateToTitleText(state: ExpenseListUIState): String {
        if (state is HasGroupInfo) {
            return state.groupName
        }
        return ""
    }

    @JvmStatic
    fun convertExpenseUIStateToTagText(state: ExpenseListUIState): String {
        if (state is HasGroupInfo) {
            return state.groupSubject
        }
        return ""
    }
}
