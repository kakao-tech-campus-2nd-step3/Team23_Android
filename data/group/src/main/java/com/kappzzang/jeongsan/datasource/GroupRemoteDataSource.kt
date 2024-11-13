package com.kappzzang.jeongsan.datasource

import com.kappzzang.jeongsan.api.GroupRetrofitService
import com.kappzzang.jeongsan.build_config.BuildConfig
import com.kappzzang.jeongsan.entity.CompleteGroupResponse
import com.kappzzang.jeongsan.entity.CreateGroupRequestDTO
import com.kappzzang.jeongsan.entity.CreateGroupResponse
import com.kappzzang.jeongsan.entity.GetGroupResponse
import com.kappzzang.jeongsan.entity.GetLinkResponse
import com.kappzzang.jeongsan.entity.GetMemberInfoResponse
import com.kappzzang.jeongsan.entity.GetTargetGroupResponse
import com.kappzzang.jeongsan.entity.GroupInfo
import com.kappzzang.jeongsan.entity.JoinGroupResponse
import com.kappzzang.jeongsan.entity.MemberInfo
import com.kappzzang.jeongsan.entity.MemberServiceIdResponse
import com.kappzzang.jeongsan.retrofit.error.AuthenticateError
import javax.inject.Inject
import retrofit2.Response

class GroupRemoteDataSource @Inject constructor(private val groupApi: GroupRetrofitService) {

    suspend fun getGroupInfo(isCompleted: Boolean): Result<List<GroupInfo>> = try {
        val response = groupApi.getGroupInfo(isCompleted = isCompleted)
        handleGetGroupInfoResponse(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun handleGetGroupInfoResponse(
        response: Response<GetGroupResponse>
    ): Result<List<GroupInfo>> = when {
        response.isSuccessful && response.body() != null -> {
            Result.success(response.body()!!.groupList)
        }

        else -> {
            Result.failure(Exception("그룹 정보를 찾을 수 없습니다."))
        }
    }

    suspend fun getTargetGroupInfo(groupId: Long) = try {
        val response = groupApi.getTargetGroupInfo(groupId = groupId)
        handleGetTargetGroupInfoResponse(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun handleGetTargetGroupInfoResponse(
        response: Response<GetTargetGroupResponse>
    ): Result<GroupInfo> = when {
        response.isSuccessful && response.body() != null -> {
            Result.success(response.body()!!.groupInfo)
        }

        else -> {
            Result.failure(Exception("모임을 찾을 수 없습니다."))
        }
    }

    suspend fun createGroup(
        groupName: String,
        groupSubject: String,
        groupMemberServiceIdList: List<String>
    ): Result<Long> = try {
        val response = groupApi.createGroup(
            CreateGroupRequestDTO(
                name = groupName,
                subject = groupSubject,
                memberIdList = groupMemberServiceIdList
            )
        )
        handleCreateGroupResponse(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun handleCreateGroupResponse(response: Response<CreateGroupResponse>): Result<Long> =
        when {
            response.isSuccessful -> {
                Result.success(response.body()!!.groupId)
            }

            response.code() in 400..499 -> {
                val errorMessage = response.body()?.message ?: "알 수 없는 오류 발생"
                Result.failure(Exception(errorMessage))
            }

            else -> {
                Result.failure(Exception("알 수 없는 오류 발생"))
            }
        }

    suspend fun completeGroup(groupId: Long): Result<Boolean> = try {
        val response = groupApi.completeGroup(groupId = groupId)
        handleCompleteGroupResponse(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun handleCompleteGroupResponse(
        response: Response<CompleteGroupResponse>
    ): Result<Boolean> = when {
        response.isSuccessful -> {
            Result.success(true)
        }

        response.code() in 400..499 -> {
            val errorMessage = response.body()?.message ?: "알 수 없는 오류 발생"
            Result.failure(Exception(errorMessage))
        }

        else -> {
            Result.failure(Exception("알 수 없는 오류 발생"))
        }
    }

    suspend fun getMemberInfo(groupId: Long): Result<List<MemberInfo>> = try {
        val response = groupApi.getMemberInfo(groupId = groupId)
        handleGetMemberInfoResponse(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun handleGetMemberInfoResponse(
        response: Response<GetMemberInfoResponse>
    ): Result<List<MemberInfo>> = when {
        response.isSuccessful && !response.body()?.memberList.isNullOrEmpty() -> {
            Result.success(response.body()!!.memberList)
        }

        response.code() in 400..499 -> {
            val errorMessage = response.body()?.message ?: "알 수 없는 오류 발생"
            Result.failure(Exception(errorMessage))
        }

        else -> {
            Result.failure(Exception("알 수 없는 오류 발생"))
        }
    }

    suspend fun joinGroup(groupId: Long): Result<Boolean> = try {
        val response = groupApi.joinGroup(groupId = groupId)
        handleJoinGroupResponse(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun handleJoinGroupResponse(response: Response<JoinGroupResponse>): Result<Boolean> =
        when {
            response.isSuccessful -> {
                Result.success(true)
            }

            response.code() in 400..499 -> {
                val errorMessage = response.body()?.message ?: "알 수 없는 오류 발생"
                Result.failure(Exception(errorMessage))
            }

            else -> {
                Result.failure(Exception("알 수 없는 오류 발생"))
            }
        }

    suspend fun getLink(): Result<String> = try {
        val response = groupApi.getLink()
        handleGetLinkResponse(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun handleGetLinkResponse(response: Response<GetLinkResponse>): Result<String> = when {
        response.isSuccessful -> {
            Result.success(response.body()!!.link)
        }

        response.code() in 400..499 -> {
            val errorMessage = response.body()?.message ?: "알 수 없는 오류 발생"
            Result.failure(Exception(errorMessage))
        }

        else -> {
            Result.failure(Exception("알 수 없는 오류 발생"))
        }
    }

    suspend fun getMemberServiceId(groupId: Long): Result<MemberServiceIdResponse> = try {
        val response = groupApi.getMemberServiceId(groupId = groupId)
        handleGetMemberServiceIdResponse(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun handleGetMemberServiceIdResponse(
        response: Response<MemberServiceIdResponse>
    ): Result<MemberServiceIdResponse> = when {
        response.isSuccessful -> {
            if (response.body() == null) {
                Result.failure(Exception("멤버 정보를 찾을 수 없습니다."))
            } else {
                Result.success(response.body()!!)
            }
        }

        response.code() in 400..499 -> {
            val errorMessage = response.body()?.message ?: "알 수 없는 오류 발생"
            Result.failure(Exception(errorMessage))
        }

        else -> {
            Result.failure(Exception("알 수 없는 오류 발생"))
        }
    }
}
