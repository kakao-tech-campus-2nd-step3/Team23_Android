package com.kappzzang.jeongsan.data.repositoryimpl

import com.kappzzang.jeongsan.domain.model.GroupItem
import com.kappzzang.jeongsan.domain.repository.GroupInfoRepository

class GroupInfoRepositoryImpl : GroupInfoRepository {
    override suspend fun getProgressingGroupInfo(): List<GroupItem> {
        // TODO: 임시로 테스트 데이터 반환 - 이후에 서버에서 받아오도록 수정
        return listOf(GroupItem("1", "캡짱모임", false, listOf(TEST_IMAGE)))
    }

    override suspend fun getDoneGroupInfo(): List<GroupItem> {
        // TODO: 임시로 테스트 데이터 반환 - 이후에 서버에서 받아오도록 수정
        return listOf(
            GroupItem("2", "모임 1", true, listOf(TEST_IMAGE)),
            GroupItem("3", "모임 2", true, listOf(TEST_IMAGE, TEST_IMAGE)),
            GroupItem("4", "모임 3", true, listOf(TEST_IMAGE, TEST_IMAGE, TEST_IMAGE))
        )
    }

    companion object {
        private const val TEST_IMAGE = "https://www.studiopeople.kr/common/img/default_profile.png"
    }
}
