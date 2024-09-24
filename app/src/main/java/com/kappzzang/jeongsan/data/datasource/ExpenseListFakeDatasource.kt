package com.kappzzang.jeongsan.data.datasource

import com.kappzzang.jeongsan.data.datasource.expense.ExpenseDatabase
import com.kappzzang.jeongsan.data.mapper.ExpenseEntityMapper
import com.kappzzang.jeongsan.domain.model.ExpenseListResponse
import com.kappzzang.jeongsan.domain.model.ExpenseState
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExpenseListFakeDatasource @Inject constructor(private val expenseDatabase: ExpenseDatabase) {

    fun getExpenseData(expenseState: ExpenseState): Flow<ExpenseListResponse> = flow {
        delay(1000)
        val result =
            when (expenseState) {
                ExpenseState.CONFIRMED -> expenseDatabase.expenseDao().getConfirmedExpense().map {
                    ExpenseEntityMapper.mapExpenseEntityToModel(it)
                }

                ExpenseState.NOT_CONFIRMED -> expenseDatabase.expenseDao().getNotConfirmedExpense()
                    .map {
                        ExpenseEntityMapper.mapExpenseEntityToModel(it)
                    }

                ExpenseState.TRANSFER_PENDING -> expenseDatabase.expenseDao().getPendingExpense()
                    .map {
                        ExpenseEntityMapper.mapExpenseEntityToModel(it)
                    }

                ExpenseState.TRANSFERED -> expenseDatabase.expenseDao().getTransferredExpense()
                    .map {
                        ExpenseEntityMapper.mapExpenseEntityToModel(it)
                    }
            }
        var totalPrice: Int = 0

        result.forEach {
            totalPrice += it.price
        }

        val fakeResponse = ExpenseListResponse(
            totalPrice = totalPrice,
            expenseList = result,
            totalExpenseToSend = totalPrice / 2
        )
        emit(fakeResponse)
    }
}
