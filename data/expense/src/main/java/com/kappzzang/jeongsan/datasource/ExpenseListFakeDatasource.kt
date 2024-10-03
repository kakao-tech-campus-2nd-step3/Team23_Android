package com.kappzzang.jeongsan.datasource

import com.kappzzang.jeongsan.datasource.expense.ExpenseDatabase
import com.kappzzang.jeongsan.mapper.ExpenseEntityMapper
import com.kappzzang.jeongsan.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ReceiptItem
import com.kappzzang.jeongsan.model.ExpenseState
import java.sql.Timestamp
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExpenseListFakeDatasource @Inject constructor(private val expenseDatabase: ExpenseDatabase) {

    fun getExpenseData(
        expenseState: ExpenseState
    ): Flow<ExpenseListResponse> = flow {
        delay(1000)
        val result =
            when (expenseState) {
                ExpenseState.CONFIRMED -> expenseDatabase.expenseDao().getConfirmedExpense()

                ExpenseState.NOT_CONFIRMED -> expenseDatabase.expenseDao().getNotConfirmedExpense()

                ExpenseState.TRANSFER_PENDING -> expenseDatabase.expenseDao().getPendingExpense()

                ExpenseState.TRANSFERED -> expenseDatabase.expenseDao().getTransferredExpense()
            }.map {
                ExpenseEntityMapper.mapExpenseEntityToModel(it)
            }
        var totalPrice = 0

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

    fun addExpense(receiptItem: ReceiptItem): String {
        val expenseEntity = com.kappzzang.jeongsan.entity.ExpenseEntity(
            name = receiptItem.title,
            totalPrice = receiptItem.expenseDetailItemList.sumOf { it.itemPrice * it.itemQuantity },
            createdTime = Timestamp(System.currentTimeMillis()).toString(),
            categoryColor = receiptItem.categoryColor,
            expenseState = ExpenseState.CONFIRMED.ordinal
        )

        expenseDatabase.expenseDao().addExpense(expenseEntity)
        return expenseEntity.id.toString()
    }
}
