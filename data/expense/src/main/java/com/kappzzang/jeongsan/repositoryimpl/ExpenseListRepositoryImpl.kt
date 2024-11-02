package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.datasource.ExpenseListRemoteDatasource
import com.kappzzang.jeongsan.entity.expenselist.ExpenseListResponseDTO
import com.kappzzang.jeongsan.mapper.ExpenseEntityMapper
import com.kappzzang.jeongsan.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.model.ReceiptItem
import com.kappzzang.jeongsan.repository.ExpenseRepository
import com.kappzzang.jeongsan.repository.ServerAuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

data class ExpenseListCachingKey(val expenseState: ExpenseState, val groupId: String)

class ExpenseListRepositoryImpl @Inject constructor(
    private val dataSource: ExpenseListRemoteDatasource,
    private val auth: ServerAuthenticationRepository
) : ExpenseRepository {

    private val cachedData = HashMap<ExpenseListCachingKey, ExpenseListResponse>()

    private fun getJwt(): String = auth.getSavedJwt()
    private fun isJwtValid(jwt: String): Boolean = (jwt != "")

    private fun mapResponseBody(body: ExpenseListResponseDTO): ExpenseListResponse {
        val expenses = body.expenseList.map { ExpenseEntityMapper.mapExpenseEntityToModel(it) }
        return ExpenseListResponse(
            totalExpenseToSend = body.totalPrice.toInt(),
            expenseList = expenses,
            totalPrice = body.totalPrice.toInt()
        )
    }

    override suspend fun uploadExpense(receiptItem: ReceiptItem, groupId: String): String {
        val jwt = getJwt()
        if (!isJwtValid(jwt)) {
            return ""
        }
        val response = dataSource.addExpense(receiptItem, jwt, groupId)
        when(response.code()){
            201 -> response.body()?.expenseId?.let {
                return it
            } ?: throw IllegalStateException("알 수 없는 오류 발생: ${response.message()}")

            400 -> throw IllegalArgumentException("유효하지 않는 입력 값")
            404 -> throw IllegalStateException(response.message())
            500 -> throw IllegalStateException(response.message())
            else -> {
                if(response.code()/100 == 2) {
                    response.body()?.expenseId?.let {
                        return it
                    } ?: throw IllegalStateException("알 수 없는 오류 발생: ${response.message()}")
                }
                else{
                    throw IllegalStateException("알 수 없는 오류 발생: ${response.message()}")
                }
            }
        }
    }

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

        val jwt = getJwt()
        if (!isJwtValid(jwt)) {
            cachedData[ExpenseListCachingKey(expenseState, groupId)] =
                ExpenseListResponse.emptyList()
        } else {
            val response = dataSource.getExpenseList(
                expenseState,
                groupId = groupId,
                jwt = jwt
            )

            response.body()?.let {
                cachedData[ExpenseListCachingKey(expenseState, groupId)] = mapResponseBody(it)
            }
        }
        emit(
            cachedData.getOrDefault(
                ExpenseListCachingKey(expenseState, groupId),
                ExpenseListResponse.emptyList()
            )
        )
    }

    override suspend fun getExpenseListToGetPaid(groupId: String): ExpenseListResponse {
        TODO("Not yet implemented")
    }
}
