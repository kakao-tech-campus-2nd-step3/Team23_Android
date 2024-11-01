package com.kappzzang.jeongsan.api

import com.kappzzang.jeongsan.entity.ExpenseDetailEntity
import com.kappzzang.jeongsan.entity.ResponseWithExpenseIdDTO
import com.kappzzang.jeongsan.entity.SaveExpensePayloadDTO
import com.kappzzang.jeongsan.entity.expenselist.ExpenseListResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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
    ): Response<ExpenseDetailEntity>

    @POST("/api/expenses/personal/{teamId}/{expenseId}")
    suspend fun updateExpenseDetails(
        @Path(value = "teamId") groupId: String,
        @Path(value = "expenseId") expenseId: String,
        @Header("accessToken") jwt: String
    )

    @GET("/api/expenses/{teamId}")
    suspend fun getExpenseList(
        @Path(value = "teamId") groupId: String,
        @Header("accessToken") jwt: String,
        @Query("state") state: String,
        @Query("isChecked") checked: Boolean
    ): Response<ExpenseListResponseDTO>

    @GET("/api/expenses/{teamId}")
    suspend fun getExpenseList(
        @Path(value = "teamId") groupId: String,
        @Header("accessToken") jwt: String,
        @Query("state") state: String
    ): Response<ExpenseListResponseDTO>
}
