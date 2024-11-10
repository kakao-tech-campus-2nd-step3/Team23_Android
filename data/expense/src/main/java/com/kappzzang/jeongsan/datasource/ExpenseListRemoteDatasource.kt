package com.kappzzang.jeongsan.datasource

import android.util.Log
import com.kappzzang.jeongsan.api.ReceiptRetrofitService
import com.kappzzang.jeongsan.entity.GetCategoryListResponseDTO
import com.kappzzang.jeongsan.entity.ImageEntity
import com.kappzzang.jeongsan.entity.ResponseWithExpenseIdDTO
import com.kappzzang.jeongsan.entity.SaveExpensePayloadDTO
import com.kappzzang.jeongsan.entity.SimpleExpenseItemEntity
import com.kappzzang.jeongsan.entity.UpdateExpenseStatePayloadDTO
import com.kappzzang.jeongsan.entity.expenselist.ExpenseListResponseDTO
import com.kappzzang.jeongsan.mapper.ExpenseDetailMapper
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.model.ReceiptItem
import com.kappzzang.jeongsan.retrofit.ResponseData
import com.kappzzang.jeongsan.util.DateConverter.formatToTransferString
import javax.inject.Inject
import retrofit2.Response

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

        Log.d("KSC", "id: $groupId, body: ${response.body()}")
        return processResponseCodeOnResponseData(response)
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
                    ExpenseDetailMapper.mapReceiptDetailItemToExpenseItemEntity(it)
                },
                paymentTime = receiptItem.paymentTime.formatToTransferString(),
                image = ImageEntity(
                    name = "empty",
                    data = receiptItem.imageBase64 ?: "",
                    url = "empty",
                    format = IMAGE_FORMAT
                ),
                categoryId = receiptItem.categoryId.toLong()
            )

            Log.d("KSC", "id: $groupId, body: $postBody")

            receiptRetrofitService.saveExpense(
                groupId = groupId,
                body = postBody
            )
        } catch (e: Exception) {
            return Result.failure(e)
        }

        return processResponseCodeOnResponseData(response)
    }

    suspend fun getCategoryList(): Result<GetCategoryListResponseDTO> {
        val response = try {
            receiptRetrofitService.getCategoryColorList()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }

        return processResponseCodeOnResponseData(response)
    }

    suspend fun updateExpenseState(
        expenseItemIdList: List<String>,
        groupId: String,
        state: ExpenseState
    ): Result<Unit> {
        val response = try {
            val body = UpdateExpenseStatePayloadDTO(
                state = mapExpenseStateToDtoState(state),
                expenses = expenseItemIdList.map {
                    SimpleExpenseItemEntity(it.toLong())
                }
            )

            receiptRetrofitService.updateExpenseState(
                groupId = groupId,
                body = body
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }
        return processResponseCode(response)
    }

    suspend fun getPurchasedExpenseList(groupId: String): Result<ExpenseListResponseDTO> {
        val response = try {
            receiptRetrofitService.getPurchasedExpenseList(groupId)
        } catch (e: Exception) {
            return Result.failure(e)
        }
        return processResponseCodeOnResponseData(response)
    }

    private fun <T> processResponseCodeOnResponseData(
        response: Response<ResponseData<T>>
    ): Result<T> {
        Log.d(
            "KSC",
            "ProcessExpenseList code: ${response.code()}, message: ${response.message()}"
        )
        when (response.code()) {
            400 -> throw IllegalArgumentException("유효하지 않는 입력 값")
            404 -> throw IllegalStateException(response.message())
            500 -> throw IllegalStateException(response.message())
            else -> {
                return if (response.code() / 100 == 2) {
                    response.body()?.let {
                        return Result.success(it.data)
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

    private fun processResponseCode(response: Response<Unit>): Result<Unit> {
        Log.d(
            "KSC",
            "ProcessExpenseList code: ${response.code()}, message: ${response.message()}"
        )
        when (response.code()) {
            400 -> throw IllegalArgumentException("유효하지 않는 입력 값")
            404 -> throw IllegalStateException(response.message())
            500 -> throw IllegalStateException(response.message())
            else -> {
                return if (response.code() / 100 == 2) {
                    return Result.success(Unit)
                } else {
                    Result.failure(IllegalStateException("알 수 없는 오류 발생: ${response.message()}"))
                }
            }
        }
    }

    companion object {
        const val IMAGE_FORMAT = "JPEG"
    }
}
