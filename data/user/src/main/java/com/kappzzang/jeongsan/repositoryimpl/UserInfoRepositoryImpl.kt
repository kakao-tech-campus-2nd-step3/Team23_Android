package com.kappzzang.jeongsan.repositoryimpl

import android.util.Log
import com.kakao.sdk.user.UserApiClient
import com.kappzzang.jeongsan.mapper.KakaoUserInfoMapper
import com.kappzzang.jeongsan.model.UserItem
import com.kappzzang.jeongsan.repository.UserInfoRepository
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserInfoRepositoryImpl @Inject constructor() : UserInfoRepository {
    override suspend fun getUserInfo(): UserItem? {
        return suspendCoroutine { continuation ->
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e(TAG, "사용자 정보 가져오기 실패", error)
                    continuation.resume(null)
                }
                else if (user != null) {
                    continuation.resume(KakaoUserInfoMapper.mapKakaoUserModelToUserItem(user))
                }
                else {
                    Log.d(TAG, "사용자 정보 없음")
                    continuation.resume(null)
                }
            }
        }
    }

    companion object {
        private const val TAG = "UserInfoRepositoryImpl"
    }
}
