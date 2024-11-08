package com.kappzzang.jeongsan.expensedetail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class ExpenseDetailViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    var groupId = ""
        private set
    var expenseId = ""
        private set
    var editable = false
        private set

    fun setInitialData(expenseId: String, groupId: String, editable: Boolean) {
        this.expenseId = expenseId
        this.groupId = groupId
        this.editable = editable
    }
}
