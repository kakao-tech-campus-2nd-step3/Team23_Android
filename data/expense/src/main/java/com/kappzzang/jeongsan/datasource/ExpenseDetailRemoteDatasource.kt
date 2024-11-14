package com.kappzzang.jeongsan.datasource

import android.util.Log
import com.kappzzang.jeongsan.api.ReceiptRetrofitService
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseDetailEntity
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseDetailSelectionInfoEntity
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseSelectionResponseDTO
import com.kappzzang.jeongsan.entity.expensedetail.UpdateExpenseDetailPayloadDTO
import com.kappzzang.jeongsan.model.ExpenseDetailItem
import com.kappzzang.jeongsan.retrofit.ResponseData
import com.kappzzang.jeongsan.retrofit.error.AuthenticateError
import com.kappzzang.jeongsan.retrofit.error.InvalidInputError
import com.kappzzang.jeongsan.retrofit.error.ItemNotFoundError
import com.kappzzang.jeongsan.retrofit.error.ServerInternalError
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

        return processResponseBodyWithData(response)
    }

    suspend fun getExpenseSelectionStatus(expenseId: String): Result<ExpenseSelectionResponseDTO> {
        val response = try {
            receiptRetrofitService.getExpenseSelectionStatus(
                expenseId = expenseId
            )
        } catch (e: Exception) {
            return (Result.failure(e))
        }

        return processResponseBodyWithData(response)
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
}
