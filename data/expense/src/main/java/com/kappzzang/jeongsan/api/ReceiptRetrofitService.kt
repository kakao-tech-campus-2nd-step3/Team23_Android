package com.kappzzang.jeongsan.api

import com.kappzzang.jeongsan.entity.ResponseWithExpenseIdDTO
import com.kappzzang.jeongsan.entity.SaveExpensePayloadDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ReceiptRetrofitService {
    @POST("/api/receipts/{teamId}")
    suspend fun saveExpense(
        @Path(value = "teamId") groupId: String,
        @Header("accessToken") jwt: String,
        @Body body: SaveExpensePayloadDTO
    ):
            Response<ResponseWithExpenseIdDTO>

    @GET("/api/receipts/items/{expenseId}")
    suspend fun getExpenseDetails(
        @Path(value = "expenseId") expenseId: String,
        @Header("accessToken") jwt: String
    )
}
