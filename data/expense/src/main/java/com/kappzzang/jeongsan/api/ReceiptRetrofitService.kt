package com.kappzzang.jeongsan.api

import com.kappzzang.jeongsan.entity.SaveExpensePayloadDTO
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ReceiptRetrofitService {
    @POST("/api/receipts/{groupId}")
    suspend fun saveExpense(@Path(value = "groupId") groupId: String,
                            @Header("accessToken")jwt: String,
                            @Body body: SaveExpensePayloadDTO)
}
