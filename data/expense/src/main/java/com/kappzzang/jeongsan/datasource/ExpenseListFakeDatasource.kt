package com.kappzzang.jeongsan.datasource

import com.kappzzang.jeongsan.datasource.expense.ExpenseDatabase
import com.kappzzang.jeongsan.entity.expenselist.ExpenseRoomEntity
import com.kappzzang.jeongsan.mapper.ExpenseDetailMapper
import com.kappzzang.jeongsan.mapper.ExpenseEntityMapper
import com.kappzzang.jeongsan.mapper.ExpenseListEntityMapper
import com.kappzzang.jeongsan.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.model.ReceiptItem
import java.sql.Timestamp
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExpenseListFakeDatasource @Inject constructor(private val expenseDatabase: ExpenseDatabase) {

    fun getExpenseData(expenseState: ExpenseState): Flow<ExpenseListResponse> = flow {
        val result =
            when (expenseState) {
                ExpenseState.CONFIRMED -> expenseDatabase.expenseDao().getConfirmedExpense()

                ExpenseState.NOT_CONFIRMED -> expenseDatabase.expenseDao().getNotConfirmedExpense()

                ExpenseState.TRANSFER_PENDING -> expenseDatabase.expenseDao().getPendingExpense()

                ExpenseState.TRANSFERED -> expenseDatabase.expenseDao().getTransferredExpense()
            }.map {
                ExpenseListEntityMapper.mapExpenseEntityToModel(it)
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

    private fun getCategoryFromId(id: String): String {
        val r = id.toIntOrNull() ?: 0
        val g = (r * 4 + 3) % 10
        val b = (g * 4 + 3) % 10
        return "#$r$r$g$g$b$b"
    }

    fun addExpense(receiptItem: ReceiptItem): String {
        val expenseEntity = ExpenseRoomEntity(
            name = receiptItem.title,
            totalPrice = receiptItem.expenseDetailItemList.sumOf { it.itemPrice * it.itemQuantity },
            createdTime = Timestamp(System.currentTimeMillis()).toString(),
            categoryColor = getCategoryFromId(receiptItem.categoryId),
            expenseState = ExpenseState.NOT_CONFIRMED.ordinal
        )

        expenseDatabase.expenseDao().addExpense(expenseEntity)
        return expenseEntity.id.toString()
    }
}
