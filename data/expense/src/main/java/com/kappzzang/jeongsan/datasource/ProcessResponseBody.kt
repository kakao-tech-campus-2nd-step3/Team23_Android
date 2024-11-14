package com.kappzzang.jeongsan.datasource

import android.util.Log
import com.kappzzang.jeongsan.retrofit.ResponseData
import com.kappzzang.jeongsan.retrofit.error.AuthenticateError
import com.kappzzang.jeongsan.retrofit.error.InvalidInputError
import com.kappzzang.jeongsan.retrofit.error.ItemNotFoundError
import com.kappzzang.jeongsan.retrofit.error.ServerInternalError
import retrofit2.Response

internal fun processResponse(response: Response<Unit>): Result<Unit> {
    Log.d(
        "KSC",
        "ProcessExpenseDetail code: ${response.code()}, message: ${response.message()}"
    )
    when (response.code()) {
        400 -> return Result.failure(InvalidInputError(response.message()))
        403 -> return Result.failure(AuthenticateError(response.message()))
        404 -> return Result.failure(ItemNotFoundError(response.message()))
        500 -> return Result.failure(ServerInternalError(response.message()))
        else -> {
            return if (response.code() / 100 == 2) {
                return Result.success(Unit)
            } else {
                Result.failure(Exception("알 수 없는 오류 발생: ${response.message()}"))
            }
        }
    }
}

internal fun <T> processResponseOnResponseData(response: Response<ResponseData<T>>): Result<T> {
    Log.d(
        "KSC",
        "ProcessExpenseDetail code: ${response.code()}, message: ${response.message()}"
    )
    when (response.code()) {
        400 -> return Result.failure(InvalidInputError(response.message()))
        403 -> return Result.failure(AuthenticateError(response.message()))
        404 -> return Result.failure(ItemNotFoundError(response.message()))
        500 -> return Result.failure(ServerInternalError(response.message()))
        else -> {
            return if (response.code() / 100 == 2) {
                response.body()?.let {
                    return Result.success(it.data)
                }
                    ?: Result.failure(
                        Exception("알 수 없는 오류 발생: ${response.message()}")
                    )
            } else {
                Result.failure(Exception("알 수 없는 오류 발생: ${response.message()}"))
            }
        }
    }
}
