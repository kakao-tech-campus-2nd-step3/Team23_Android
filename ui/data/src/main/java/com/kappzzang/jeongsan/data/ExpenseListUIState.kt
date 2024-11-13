package com.kappzzang.jeongsan.data

import com.kappzzang.jeongsan.model.ExpenseState

interface HasGroupId {
    val groupId: String
}

interface HasGroupInfo {
    val groupName: String
    val groupSubject: String
}

sealed class ExpenseListUIState {
    data object Initial : ExpenseListUIState()

    data class FetchingGroupUIItem(override val groupId: String) :
        ExpenseListUIState(),
        HasGroupId

    data class Idle(
        override val groupId: String,
        override val groupName: String,
        override val groupSubject: String
    ) : ExpenseListUIState(),
        HasGroupId,
        HasGroupInfo

    data class SelectingExpense(
        override val groupId: String,
        override val groupName: String,
        override val groupSubject: String,
        val selectedExpenseId: String,
        val expenseState: ExpenseState,
        val isPayer: Boolean
    ) : ExpenseListUIState(),
        HasGroupId,
        HasGroupInfo

    data class Completing(
        override val groupId: String,
        override val groupName: String,
        override val groupSubject: String
    ) : ExpenseListUIState(),
        HasGroupId,
        HasGroupInfo

    data class CompleteSuccess(override val groupName: String, override val groupSubject: String) :
        ExpenseListUIState(),
        HasGroupInfo

    data class CompleteFailed(val message: String) : ExpenseListUIState()
}
