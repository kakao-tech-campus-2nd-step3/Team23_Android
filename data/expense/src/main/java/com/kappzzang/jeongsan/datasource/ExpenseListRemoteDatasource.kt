package com.kappzzang.jeongsan.datasource

import com.kappzzang.jeongsan.api.ReceiptRetrofitService
import com.kappzzang.jeongsan.entity.ImageEntity
import com.kappzzang.jeongsan.entity.ResponseWithExpenseIdDTO
import com.kappzzang.jeongsan.entity.SaveExpensePayloadDTO
import com.kappzzang.jeongsan.entity.expenselist.ExpenseListResponseDTO
import com.kappzzang.jeongsan.mapper.ExpenseEntityMapper
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.model.ReceiptItem
import com.kappzzang.jeongsan.util.DateConverter.formatToTransferString
import retrofit2.Response
import javax.inject.Inject

class ExpenseListRemoteDatasource @Inject constructor(
    private val receiptRetrofitService: ReceiptRetrofitService
) {

    private fun mapExpenseStateToDtoState(state: ExpenseState): String = when (state) {
        ExpenseState.CONFIRMED -> "ongoing"
        ExpenseState.NOT_CONFIRMED -> "ongoing"
        ExpenseState.TRANSFER_PENDING -> "pending"
        ExpenseState.TRANSFERED -> "completed"
    }

    suspend fun getExpenseList(
        expenseState: ExpenseState,
        groupId: String
    ): Result<ExpenseListResponseDTO> {
        val response = try {
            receiptRetrofitService.getExpenseList(
                groupId = groupId,
                state = mapExpenseStateToDtoState(expenseState),
                checked = checkIsChecked(expenseState)
            )
        } catch (e: Exception) {
            return Result.failure(e)
        }

        return processResponseCode(response)
    }

    private fun checkIsChecked(state: ExpenseState): Boolean? = when (state) {
        ExpenseState.CONFIRMED -> true
        ExpenseState.NOT_CONFIRMED -> false
        else -> null
    }

    suspend fun addExpense(
        receiptItem: ReceiptItem,
        groupId: String
    ): Result<ResponseWithExpenseIdDTO> {
        val response = try {
            val postBody = SaveExpensePayloadDTO(
                title = receiptItem.title,
                items = receiptItem.expenseDetailItemList.map {
                    ExpenseEntityMapper.mapReceiptDetailItemToExpenseItemEntity(it)
                },
                paymentTime = receiptItem.paymentTime.formatToTransferString(),
                image = ImageEntity(
                    name = "",
                    data = receiptItem.imageBase64 ?: "",
                    url = "",
                    format = IMAGE_FORMAT
                ),
                categoryId = CATEGORY_ID
            )

            receiptRetrofitService.saveExpense(
                groupId = groupId,
                body = postBody
            )
        } catch (e: Exception) {
            return Result.failure(e)
        }

        return processResponseCode(response)
    }

    private fun <T> processResponseCode(response: Response<T>): Result<T> {
        when (response.code()) {
            400 -> throw IllegalArgumentException("유효하지 않는 입력 값")
            404 -> throw IllegalStateException(response.message())
            500 -> throw IllegalStateException(response.message())

            else -> {
                if (response.code() / 100 == 2) {
                    response.body()?.let {
                        return Result.success<T>(it)
                    }
                        ?: return Result.failure(
                            IllegalStateException("알 수 없는 오류 발생: ${response.message()}")
                        )
                } else {
                    return Result.failure(
                        IllegalStateException("알 수 없는 오류 발생: ${response.message()}")
                    )
                }
            }
        }
    }

    companion object {
        const val IMAGE_FORMAT = "JPG"
        const val CATEGORY_ID = 0L
    }
}
