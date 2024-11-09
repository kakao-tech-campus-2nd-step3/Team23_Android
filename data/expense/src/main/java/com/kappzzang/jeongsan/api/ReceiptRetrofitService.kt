package com.kappzzang.jeongsan.api

import com.kappzzang.jeongsan.entity.GetCategoryListResponseDTO
import com.kappzzang.jeongsan.entity.ResponseWithExpenseIdDTO
import com.kappzzang.jeongsan.entity.SaveExpensePayloadDTO
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseDetailEntity
import com.kappzzang.jeongsan.entity.expensedetail.UpdateExpenseDetailPayloadDTO
import com.kappzzang.jeongsan.entity.expenselist.ExpenseListResponseDTO
import com.kappzzang.jeongsan.retrofit.ResponseData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReceiptRetrofitService {
    @POST("/api/receipts/{teamId}")
    suspend fun saveExpense(
        @Path(value = "teamId") groupId: String,
        @Body body: SaveExpensePayloadDTO
    ): Response<ResponseData<ResponseWithExpenseIdDTO>>

    @GET("/api/receipts/items/{expenseId}")
    suspend fun getExpenseDetail(
        @Path(value = "expenseId") expenseId: String
    ): Response<ResponseData<ExpenseDetailEntity>>

    @POST("/api/expenses/personal/{teamId}/{expenseId}")
    suspend fun updateExpenseDetail(
        @Path(value = "teamId") groupId: String,
        @Path(value = "expenseId") expenseId: String,
        @Body body: UpdateExpenseDetailPayloadDTO
    ): Response<Unit>

    @GET("/api/expenses/{teamId}")
    suspend fun getExpenseList(
        @Path(value = "teamId") groupId: String,
        @Query("state") state: String,
        @Query("isChecked") checked: Boolean?
    ): Response<ResponseData<ExpenseListResponseDTO>>

    @GET("/api/expenses/categories")
    suspend fun getCategoryColorList(): Response<ResponseData<GetCategoryListResponseDTO>>
}
