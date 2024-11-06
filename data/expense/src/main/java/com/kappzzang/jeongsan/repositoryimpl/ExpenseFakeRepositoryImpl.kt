package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.datasource.ExpenseListFakeDatasource
import com.kappzzang.jeongsan.model.ExpenseCategory
import com.kappzzang.jeongsan.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.model.ReceiptItem
import com.kappzzang.jeongsan.repository.ExpenseRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last

class ExpenseFakeRepositoryImpl @Inject constructor(
    private val dataSource: ExpenseListFakeDatasource
) : ExpenseRepository {

    private val cachedData = HashMap<ExpenseListCachingKey, ExpenseListResponse>()

    private fun getFakeCategoryList(): List<ExpenseCategory> = listOf(
        ExpenseCategory(id = "1", color = "#ff0000", name = "편의점"),
        ExpenseCategory(id = "2", color = "#4f6622", name = "영화관"),
        ExpenseCategory(id = "3", color = "#7777ff", name = "숙박"),
        ExpenseCategory(id = "4", color = "#999999", name = "기타")
    )

    override fun getExpenseList(
        groupId: String,
        expenseState: ExpenseState
    ): Flow<Result<ExpenseListResponse>> = flow {
        emit(
            Result.success(
                cachedData.getOrDefault(
                    ExpenseListCachingKey(expenseState, groupId),
                    ExpenseListResponse.emptyList()
                )
            )
        )
        dataSource.getExpenseData(expenseState).collect {
            cachedData[ExpenseListCachingKey(expenseState, groupId)] = it
        }
        emit(
            Result.success(
                cachedData.getOrDefault(
                    ExpenseListCachingKey(expenseState, groupId),
                    ExpenseListResponse.emptyList()
                )
            )
        )
    }

    override suspend fun getExpenseListToGetPaid(groupId: String): Result<ExpenseListResponse> =
        Result.success(
            dataSource.getExpenseData(
                ExpenseState.TRANSFER_PENDING
            ).last()
        )

    override suspend fun uploadExpense(receiptItem: ReceiptItem, groupId: String): Result<String> =
        Result.success(dataSource.addExpense(receiptItem))

    override suspend fun getExpenseCategoryList(): Result<List<ExpenseCategory>> =
        Result.success(getFakeCategoryList())
}
