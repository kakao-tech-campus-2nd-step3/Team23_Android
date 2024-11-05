package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.datasource.ExpenseListRemoteDatasource
import com.kappzzang.jeongsan.entity.expenselist.ExpenseListResponseDTO
import com.kappzzang.jeongsan.mapper.ExpenseEntityMapper
import com.kappzzang.jeongsan.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.model.ReceiptItem
import com.kappzzang.jeongsan.repository.ExpenseRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class ExpenseListCachingKey(val expenseState: ExpenseState, val groupId: String)

class ExpenseListRepositoryImpl @Inject constructor(
    private val dataSource: ExpenseListRemoteDatasource
) : ExpenseRepository {

    private val cachedData = HashMap<ExpenseListCachingKey, ExpenseListResponse>()

    override suspend fun uploadExpense(receiptItem: ReceiptItem, groupId: String): Result<String> =
        dataSource.addExpense(receiptItem, groupId)
            .mapCatching {
                ExpenseEntityMapper.mapResponseWithExpenseEntityToModel(it)
            }

    override fun getExpenseList(
        groupId: String,
        expenseState: ExpenseState
    ): Flow<Result<ExpenseListResponse>> = flow {
        // 먼저 캐싱된 데이터 emit
        emit(
            Result.success(
                cachedData.getOrDefault(
                    ExpenseListCachingKey(expenseState, groupId),
                    ExpenseListResponse.emptyList()
                )
            )
        )

        // Remote API로 지출 목록 불러오고 캐싱 데이터 갱신

        val response = getExpenseListResponseFromAPI(groupId, expenseState)
        response.onSuccess {
            cachedData[ExpenseListCachingKey(expenseState, groupId)] = it
        }

        emit(response)
    }

    private suspend fun getExpenseListResponseFromAPI(
        groupId: String,
        expenseState: ExpenseState
    ): Result<ExpenseListResponse> = dataSource.getExpenseList(
        expenseState,
        groupId = groupId
    ).mapCatching {
        mapResponseBody(it)
    }

    private fun mapResponseBody(body: ExpenseListResponseDTO): ExpenseListResponse {
        val expenses = body.expenseList.map { ExpenseEntityMapper.mapExpenseEntityToModel(it) }
        return ExpenseListResponse(
            totalExpenseToSend = body.totalPrice.toInt(),
            expenseList = expenses,
            totalPrice = body.totalPrice.toInt()
        )
    }

    override suspend fun getExpenseListToGetPaid(groupId: String): Result<ExpenseListResponse> {
        TODO("Not yet implemented")
    }
}
