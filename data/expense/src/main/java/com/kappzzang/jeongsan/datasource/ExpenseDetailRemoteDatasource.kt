package com.kappzzang.jeongsan.datasource

import com.kappzzang.jeongsan.api.ReceiptRetrofitService
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseDetailEntity
import javax.inject.Inject

class ExpenseDetailRemoteDatasource @Inject constructor(
    private val receiptRetrofitService: ReceiptRetrofitService
) {
    suspend fun getExpenseDetail(expenseId: String): Result<ExpenseDetailEntity> {
        val response = try {
            receiptRetrofitService.getExpenseDetail(
                expenseId = expenseId
            )
        } catch (e: Exception) {
            return(Result.failure(e))
        }

        when (response.code()) {
            404 -> return Result.failure(IllegalStateException("존재하지 않는 지출"))
            500 -> return Result.failure(IllegalStateException(response.message()))
            else -> {
                return if (response.code() / 100 == 2) {
                    response.body()?.let {
                        return Result.success(it)
                    } ?: Result.failure(IllegalStateException("알 수 없는 오류 발생: ${response.message()}"))
                } else {
                    Result.failure(IllegalStateException("알 수 없는 오류 발생: ${response.message()}"))
                }
            }
        }
    }
}
