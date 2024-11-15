package com.kappzzang.jeongsan.mapper

import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.user.model.User
import com.kappzzang.jeongsan.model.UserFriendItem
import com.kappzzang.jeongsan.model.UserItem

object KakaoUserInfoMapper {
    fun mapKakaoUserModelToUserItem(user: User): UserItem = UserItem(
        serviceId = user.id?.toString() ?: "",
        name = user.kakaoAccount?.profile?.nickname ?: "",
        email = user.kakaoAccount?.email ?: "",
        profileUrl = user.kakaoAccount?.profile?.profileImageUrl ?: ""
    )

    fun Friend.toUserFriendItem(): UserFriendItem = UserFriendItem(
        uuid = uuid,
        serviceId = id?.toString() ?: ""
    )
}
