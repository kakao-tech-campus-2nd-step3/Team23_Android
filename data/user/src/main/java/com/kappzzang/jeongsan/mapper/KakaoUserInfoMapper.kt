package com.kappzzang.jeongsan.mapper

import com.kakao.sdk.user.model.User
import com.kappzzang.jeongsan.model.UserItem

object KakaoUserInfoMapper {
    fun mapKakaoUserModelToUserItem(user: User): UserItem = UserItem(
        uuid = user.uuid ?: "",
        name = user.kakaoAccount?.profile?.nickname ?: "",
        profileUrl = user.kakaoAccount?.profile?.profileImageUrl ?: ""
    )
}
