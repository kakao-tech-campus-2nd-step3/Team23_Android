package com.kappzzang.jeongsan.data.repositoryimpl

import com.kappzzang.jeongsan.data.datasource.ExpenseListFakeDatasource
import com.kappzzang.jeongsan.domain.model.ExpenseListResponse
import com.kappzzang.jeongsan.domain.model.ExpenseState
import com.kappzzang.jeongsan.domain.repository.ExpenseListRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class ExpenseListCachingKey(val expenseState: ExpenseState, val groupId: String)

class ExpenseListFakeRepositoryImpl @Inject constructor(
    private val dataSource: ExpenseListFakeDatasource
) : ExpenseListRepository {

    private val cachedData = HashMap<ExpenseListCachingKey, ExpenseListResponse>()

    override fun getExpenseList(
        groupId: String,
        expenseState: ExpenseState
    ): Flow<ExpenseListResponse> = flow {
        emit(
            cachedData.getOrDefault(
                ExpenseListCachingKey(expenseState, groupId),
                ExpenseListResponse.emptyList()
            )
        )
        dataSource.getExpenseData(expenseState).collect {
            cachedData[ExpenseListCachingKey(expenseState, groupId)] = it
        }
        emit(
            cachedData.getOrDefault(
                ExpenseListCachingKey(expenseState, groupId),
                ExpenseListResponse.emptyList()
            )
        )
    }
}