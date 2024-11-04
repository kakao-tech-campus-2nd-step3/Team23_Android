package com.kappzzang.jeongsan.datasource

import com.kappzzang.jeongsan.api.ReceiptRetrofitService
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseDetailEntity
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseDetailSelectionInfoEntity
import com.kappzzang.jeongsan.entity.expensedetail.UpdateExpenseDetailPayloadDTO
import com.kappzzang.jeongsan.model.ExpenseDetailItem
import javax.inject.Inject
import retrofit2.Response

class ExpenseDetailRemoteDatasource @Inject constructor(
    private val receiptRetrofitService: ReceiptRetrofitService
) {
    suspend fun getExpenseDetail(expenseId: String): Result<ExpenseDetailEntity> {
        val response = try {
            receiptRetrofitService.getExpenseDetail(
                expenseId = expenseId
            )
        } catch (e: Exception) {
            return (Result.failure(e))
        }

        return processResponseBody(response)
    }

    suspend fun updateExpenseDetail(
        expenseId: String,
        groupId: String,
        edited: List<ExpenseDetailItem>
    ): Result<Unit> {
        val response = try {
            val body = UpdateExpenseDetailPayloadDTO(
                items = edited.map {
                    ExpenseDetailSelectionInfoEntity(
                        quantity = it.selectedQuantity,
                        itemId = it.id.toInt()
                    )
                }
            )
            receiptRetrofitService.updateExpenseDetail(
                expenseId = expenseId,
                groupId = groupId,
                body = body
            )
        } catch (e: Exception) {
            return (Result.failure(e))
        }

        return processResponseBody(response)
    }

    private fun <T> processResponseBody(response: Response<T>): Result<T> {
        when (response.code()) {
            404 -> return Result.failure(IllegalStateException("존재하지 않는 지출"))
            500 -> return Result.failure(IllegalStateException(response.message()))
            else -> {
                return if (response.code() / 100 == 2) {
                    response.body()?.let {
                        return Result.success(it)
                    }
                        ?: Result.failure(
                            IllegalStateException("알 수 없는 오류 발생: ${response.message()}")
                        )
                } else {
                    Result.failure(IllegalStateException("알 수 없는 오류 발생: ${response.message()}"))
                }
            }
        }
    }
}
