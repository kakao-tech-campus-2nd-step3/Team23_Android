package com.kappzzang.jeongsan.data.repositoryimpl

import com.kappzzang.jeongsan.domain.model.UserItem
import com.kappzzang.jeongsan.domain.repository.UserInfoRepository
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor() : UserInfoRepository {
    override suspend fun getUserInfo(): UserItem {
        // TODO: 임시로 테스트 데이터 반환 - 이후에 카카오 SDK에서 받아오도록 수정
        // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info-response
        return UserItem("테스트 사용자", "https://www.studiopeople.kr/common/img/default_profile.png")
    }
}
