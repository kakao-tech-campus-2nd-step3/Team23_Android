package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.datasource.ExpenseListRemoteDatasource
import com.kappzzang.jeongsan.entity.expenselist.ExpenseListResponseDTO
import com.kappzzang.jeongsan.mapper.ExpenseEntityMapper
import com.kappzzang.jeongsan.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.model.ReceiptItem
import com.kappzzang.jeongsan.repository.ExpenseRepository
import com.kappzzang.jeongsan.repository.ServerAuthenticationRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class ExpenseListCachingKey(val expenseState: ExpenseState, val groupId: String)

class ExpenseListRepositoryImpl @Inject constructor(
    private val dataSource: ExpenseListRemoteDatasource,
    private val auth: ServerAuthenticationRepository
) : ExpenseRepository {

    private val cachedData = HashMap<ExpenseListCachingKey, ExpenseListResponse>()

    private fun getJwt(): String = auth.getSavedJwt()
    private fun isJwtValid(jwt: String): Boolean = (jwt != "")

    override suspend fun uploadExpense(receiptItem: ReceiptItem, groupId: String): String {
        val jwt = getJwt()
        if (!isJwtValid(jwt)) {
            return ""
        }
        val response = dataSource.addExpense(receiptItem, jwt, groupId)
        when (response.code()) {
            201 -> response.body()?.expenseId?.let {
                return it
            } ?: throw IllegalStateException("알 수 없는 오류 발생: ${response.message()}")

            400 -> throw IllegalArgumentException("유효하지 않는 입력 값")
            404 -> throw IllegalStateException(response.message())
            500 -> throw IllegalStateException(response.message())
            else -> {
                if (response.code() / 100 == 2) {
                    response.body()?.expenseId?.let {
                        return it
                    } ?: throw IllegalStateException("알 수 없는 오류 발생: ${response.message()}")
                } else {
                    throw IllegalStateException("알 수 없는 오류 발생: ${response.message()}")
                }
            }
        }
    }

    override fun getExpenseList(
        groupId: String,
        expenseState: ExpenseState
    ): Flow<ExpenseListResponse> = flow {
        // 먼저 캐싱된 데이터 emit
        emit(
            cachedData.getOrDefault(
                ExpenseListCachingKey(expenseState, groupId),
                ExpenseListResponse.emptyList()
            )
        )

        // Remote API로 지출 목록 불러오고 캐싱 데이터 갱신
        val jwt = getJwt()
        if (!isJwtValid(jwt)) {
            cachedData[ExpenseListCachingKey(expenseState, groupId)] =
                ExpenseListResponse.emptyList()
        } else {
            val response = getExpenseListResponseFromAPI(groupId, expenseState, jwt)
            cachedData[ExpenseListCachingKey(expenseState, groupId)] = response
        }

        // 갱신된 캐싱 데이터 emit
        emit(
            cachedData.getOrDefault(
                ExpenseListCachingKey(expenseState, groupId),
                ExpenseListResponse.emptyList()
            )
        )
    }

    private suspend fun getExpenseListResponseFromAPI(
        groupId: String,
        expenseState: ExpenseState,
        jwt: String
    ): ExpenseListResponse {
        val response = dataSource.getExpenseList(
            expenseState,
            groupId = groupId,
            jwt = jwt
        )

        when (response.code()) {
            201 -> response.body()?.let {
                return mapResponseBody(it)
            } ?: throw IllegalStateException("알 수 없는 오류 발생: ${response.message()}")

            400 -> throw IllegalArgumentException("유효하지 않는 입력 값")
            404 -> throw IllegalStateException("유효하지 않는 teamId")
            else -> {
                if (response.code() / 100 == 2) {
                    response.body()?.let {
                        return mapResponseBody(it)
                    } ?: throw IllegalStateException("알 수 없는 오류 발생: ${response.message()}")
                } else {
                    throw IllegalStateException("알 수 없는 오류 발생: ${response.message()}")
                }
            }
        }
    }

    private fun mapResponseBody(body: ExpenseListResponseDTO): ExpenseListResponse {
        val expenses = body.expenseList.map { ExpenseEntityMapper.mapExpenseEntityToModel(it) }
        return ExpenseListResponse(
            totalExpenseToSend = body.totalPrice.toInt(),
            expenseList = expenses,
            totalPrice = body.totalPrice.toInt()
        )
    }

    override suspend fun getExpenseListToGetPaid(groupId: String): ExpenseListResponse {
        TODO("Not yet implemented")
    }
}
