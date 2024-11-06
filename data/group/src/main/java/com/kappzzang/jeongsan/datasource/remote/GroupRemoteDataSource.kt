package com.kappzzang.jeongsan.datasource.remote

import com.kappzzang.jeongsan.api.GroupRetrofitService
import com.kappzzang.jeongsan.entity.CompleteGroupResponse
import com.kappzzang.jeongsan.entity.CreateGroupResponse
import com.kappzzang.jeongsan.entity.GetGroupResponse
import com.kappzzang.jeongsan.entity.GetLinkResponse
import com.kappzzang.jeongsan.entity.GetMemberInfoResponse
import com.kappzzang.jeongsan.entity.GroupInfo
import com.kappzzang.jeongsan.entity.JoinGroupRequest
import com.kappzzang.jeongsan.entity.JoinGroupResponse
import com.kappzzang.jeongsan.entity.MemberInfo
import javax.inject.Inject
import retrofit2.Response

class GroupRemoteDataSource @Inject constructor(private val groupApi: GroupRetrofitService) {
    suspend fun getGroupInfo(jwt: String, isCompleted: Boolean): Result<List<GroupInfo>> = try {
        val response = groupApi.getGroupInfo(
            token = jwt,
            isCompleted = isCompleted
        )
        handleGetGroupInfoResponse(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun handleGetGroupInfoResponse(
        response: Response<GetGroupResponse>
    ): Result<List<GroupInfo>> = when {
        response.isSuccessful && !response.body()?.groupList.isNullOrEmpty() -> {
            Result.success(response.body()!!.groupList)
        }
        else -> {
            Result.failure(Exception("그룹 정보를 가져오는데 실패"))
        }
    }

    suspend fun createGroup(
        jwt: String,
        groupName: String,
        groupSubject: String,
        groupMemberId: List<Long>
    ): Result<Long> = try {
        val response = groupApi.createGroup(
            token = jwt,
            name = groupName,
            subject = groupSubject,
            memberIdList = groupMemberId
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
            response.code() == 404 -> {
                Result.failure(Exception("유저를 찾을 수 없음"))
            }
            // 겹쳐도 되기로 했던 것 같은데
            response.code() == 409 -> {
                Result.failure(Exception("중복된 모임 이름이 존재"))
            }
            else -> {
                Result.failure(Exception("알수없는 오류 발생"))
            }
        }

    suspend fun completeGroup(jwt: String, groupId: Long): Result<Boolean> = try {
        val response = groupApi.completeGroup(
            token = jwt,
            groupId = groupId
        )
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
        response.code() == 400 -> {
            Result.failure(Exception("모임이 이미 종료된 상태"))
        }
        response.code() == 404 -> {
            Result.failure(Exception("완료하고자 하는 모임을 찾을 수 없음"))
        }
        else -> {
            Result.failure(Exception("알수없는 오류 발생"))
        }
    }

    suspend fun getMemberInfo(jwt: String, groupId: Long): Result<List<MemberInfo>> = try {
        val response = groupApi.getMemberInfo(
            token = jwt,
            groupId = groupId
        )
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
        response.code() == 404 -> {
            Result.failure(Exception("모임의 멤버 초대 현황 목록을 찾을 수 없음"))
        }
        else -> {
            Result.failure(Exception("알수없는 오류 발생"))
        }
    }

    suspend fun joinGroup(jwt: String, groupId: Long, myId: Long): Result<Boolean> = try {
        val response = groupApi.joinGroup(
            token = jwt,
            groupId = groupId,
            request = JoinGroupRequest(myId)
        )
        handleJoinGroupResponse(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun handleJoinGroupResponse(response: Response<JoinGroupResponse>): Result<Boolean> =
        when {
            response.isSuccessful -> {
                Result.success(true)
            }
            response.code() == 400 -> {
                Result.failure(Exception("모임에 초대 되지 않은 유저"))
            }
            response.code() == 404 -> {
                Result.failure(Exception("잘못된 memberId, 사용자를 찾을 수 없음"))
            }
            else -> {
                Result.failure(Exception("알수없는 오류 발생"))
            }
        }

    suspend fun getLink(jwt: String): Result<String> = try {
        val response = groupApi.getLink(
            token = jwt
        )
        handleGetLinkResponse(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun handleGetLinkResponse(response: Response<GetLinkResponse>): Result<String> = when {
        response.isSuccessful -> {
            Result.success(response.body()!!.link)
        }
        response.code() == 404 -> {
            Result.failure(Exception("카카오 페이 송금 링크를 찾을 수 없음"))
        }
        else -> {
            Result.failure(Exception("알수없는 오류 발생"))
        }
    }
}
