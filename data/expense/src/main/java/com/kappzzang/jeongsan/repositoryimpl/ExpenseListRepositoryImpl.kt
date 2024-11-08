package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.datasource.ExpenseListRemoteDatasource
import com.kappzzang.jeongsan.entity.expenselist.ExpenseListResponseDTO
import com.kappzzang.jeongsan.mapper.ExpenseEntityMapper
import com.kappzzang.jeongsan.mapper.ExpenseListEntityMapper
import com.kappzzang.jeongsan.model.ExpenseCategory
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

    override suspend fun getExpenseCategoryList(): Result<List<ExpenseCategory>> =
        dataSource.getCategoryList().mapCatching {
            it.categoryList.map { category ->
                ExpenseEntityMapper.mapCategoryToModel(category)
            }
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
            emit(response)
        }
            .onFailure {
                it.printStackTrace()
            }
    }

    override suspend fun forceGetExpenseList(
        groupId: String,
        expenseState: ExpenseState
    ): Result<ExpenseListResponse> {
        val response = getExpenseListResponseFromAPI(groupId, expenseState)
        response.fold(
            onSuccess = {
                cachedData[ExpenseListCachingKey(expenseState, groupId)] = it
                return Result.success(it)
            },
            onFailure = {
                it.printStackTrace()
                return Result.failure(it)
            }
        )
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

    private fun getUuid(): String {
        TODO("UUID 조회 구현")
    }

    private fun mapResponseBody(body: ExpenseListResponseDTO): ExpenseListResponse {
        val expenses = body.expenseList.map {
            ExpenseListEntityMapper.mapExpenseEntityToModel(it, getUuid())
        }
        return ExpenseListResponse(
            totalExpenseToSend = body.myTotalExpense ?: 0,
            expenseList = expenses,
            totalPrice = body.totalPrice.toInt()
        )
    }

    override suspend fun getExpenseListToGetPaid(groupId: String): Result<ExpenseListResponse> {
        TODO("Not yet implemented")
    }
}
