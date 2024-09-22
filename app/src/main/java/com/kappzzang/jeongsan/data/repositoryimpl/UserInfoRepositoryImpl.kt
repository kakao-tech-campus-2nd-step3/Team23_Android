package com.kappzzang.jeongsan.data.repositoryimpl

import com.kappzzang.jeongsan.domain.repository.UserInfoRepository

class UserInfoRepositoryImpl : UserInfoRepository {
    override suspend fun getUserName(): String {
        // TODO: 임시로 테스트 데이터 반환 - 이후에 카카오 SDK에서 받아오도록 수정
        return "테스트 사용자"
    }
}
