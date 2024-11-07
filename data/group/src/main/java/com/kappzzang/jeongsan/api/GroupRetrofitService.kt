package com.kappzzang.jeongsan.api

import com.kappzzang.jeongsan.entity.CompleteGroupResponse
import com.kappzzang.jeongsan.entity.CreateGroupResponse
import com.kappzzang.jeongsan.entity.GetGroupResponse
import com.kappzzang.jeongsan.entity.GetLinkResponse
import com.kappzzang.jeongsan.entity.GetMemberInfoResponse
import com.kappzzang.jeongsan.entity.GetMyExpenseResponse
import com.kappzzang.jeongsan.entity.GetTargetGroupResponse
import com.kappzzang.jeongsan.entity.JoinGroupRequest
import com.kappzzang.jeongsan.entity.JoinGroupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupRetrofitService {
    @GET("/api/teams")
    suspend fun getGroupInfo(@Query("isClosed") isCompleted: Boolean): Response<GetGroupResponse>

    @GET("/api/teams/{teamId}")
    suspend fun getTargetGroupInfo(@Path("teamId") groupId: Long): Response<GetTargetGroupResponse>

    @POST("/api/teams")
    suspend fun createGroup(
        @Query("name") name: String,
        @Query("subject") subject: String,
        @Query("members") memberIdList: List<String>
    ): Response<CreateGroupResponse>

    @PATCH("/api/teams/{teamId}")
    suspend fun completeGroup(@Path("teamId") groupId: Long): Response<CompleteGroupResponse>

    @GET("/api/teams/{teamId}/members")
    suspend fun getMemberInfo(@Path("teamId") groupId: Long): Response<GetMemberInfoResponse>

    @POST("/api/members/join/{teamId}")
    suspend fun joinGroup(
        @Path("teamId") groupId: Long,
        @Body request: JoinGroupRequest
    ): Response<JoinGroupResponse>

    @GET("/api/members/link")
    suspend fun getLink(): Response<GetLinkResponse>

    // expense모듈에 속해야하는 것 같아 구현을 마치지 않음
    @GET("/api/expenses/ipaid/{teamId}")
    suspend fun getMyExpense(): Response<GetMyExpenseResponse>
}
