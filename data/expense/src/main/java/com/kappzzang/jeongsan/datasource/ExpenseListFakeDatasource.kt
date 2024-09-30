package com.kappzzang.jeongsan.datasource

import com.kappzzang.jeongsan.datasource.expense.ExpenseDatabase
import com.kappzzang.jeongsan.domain.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.domain.model.ReceiptItem
import java.sql.Timestamp
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExpenseListFakeDatasource @Inject constructor(private val expenseDatabase: ExpenseDatabase) {

    fun getExpenseData(expenseState: com.kappzzang.jeongsan.model.ExpenseState): Flow<ExpenseListResponse> = flow {
        delay(1000)
        val result =
            when (expenseState) {
                com.kappzzang.jeongsan.model.ExpenseState.CONFIRMED -> expenseDatabase.expenseDao().getConfirmedExpense()

                com.kappzzang.jeongsan.model.ExpenseState.NOT_CONFIRMED -> expenseDatabase.expenseDao().getNotConfirmedExpense()

                com.kappzzang.jeongsan.model.ExpenseState.TRANSFER_PENDING -> expenseDatabase.expenseDao().getPendingExpense()

                com.kappzzang.jeongsan.model.ExpenseState.TRANSFERED -> expenseDatabase.expenseDao().getTransferredExpense()
            }.map {
                com.kappzzang.jeongsan.mapper.ExpenseEntityMapper.mapExpenseEntityToModel(it)
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
            expenseState = com.kappzzang.jeongsan.model.ExpenseState.CONFIRMED.ordinal
        )

        expenseDatabase.expenseDao().addExpense(expenseEntity)
        return expenseEntity.id.toString()
    }
}
