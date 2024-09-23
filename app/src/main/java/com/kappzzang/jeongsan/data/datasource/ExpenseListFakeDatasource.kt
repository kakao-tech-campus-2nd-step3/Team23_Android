package com.kappzzang.jeongsan.data.datasource

import com.kappzzang.jeongsan.domain.model.ExpenseItem
import com.kappzzang.jeongsan.domain.model.ExpenseListResponse
import com.kappzzang.jeongsan.domain.model.ExpenseState
import com.kappzzang.jeongsan.domain.model.MemberItem
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExpenseListFakeDatasource @Inject constructor() {
    private val nameList = listOf("스타벅스", "GS25", "빽다방", "롯데시네마", "CGV", "롯데리아", "버거킹")

    private fun makeFakeItemWithState(expenseState: ExpenseState, prefix: Int): ExpenseItem {
        val adjustedIndex = (prefix + 1) * (ExpenseState.entries.indexOf(expenseState) + 1)

        return ExpenseItem(
            id = adjustedIndex.toString(),
            name = "${nameList[adjustedIndex % nameList.size]}$prefix",
            payer = MemberItem("123", "test", "", true),
            price = 1200 * adjustedIndex,
            date = Date(2024, 9, 1 + prefix % 30, 10, 30),
            state = expenseState,
            categoryColor = "#ffbbcc"
        )
    }

    private fun makeFakeItemListWithState(
        count: Int,
        expenseState: ExpenseState
    ): List<ExpenseItem> = (1..count).map {
        makeFakeItemWithState(expenseState, it)
    }

    fun getExpenseData(expenseState: ExpenseState): Flow<ExpenseListResponse> = flow {
        val result =
            when (expenseState) {
                ExpenseState.CONFIRMED -> ExpenseListResponse(
                    120000,
                    0,
                    makeFakeItemListWithState(10, expenseState)
                )

                ExpenseState.NOT_CONFIRMED -> ExpenseListResponse(
                    120000,
                    0,
                    makeFakeItemListWithState(5, expenseState)
                )

                ExpenseState.TRANSFER_PENDING -> ExpenseListResponse(
                    120000,
                    60000,
                    makeFakeItemListWithState(8, expenseState)
                )

                ExpenseState.TRANSFERED -> ExpenseListResponse(
                    120000,
                    0,
                    makeFakeItemListWithState(20, expenseState)
                )
            }
        delay(1500)
        emit(result)
    }
}
