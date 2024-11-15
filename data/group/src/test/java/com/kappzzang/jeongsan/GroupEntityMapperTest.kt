package com.kappzzang.jeongsan

import com.kappzzang.jeongsan.entity.GroupInfo
import com.kappzzang.jeongsan.entity.MemberPreview
import com.kappzzang.jeongsan.entity.MemberServiceIdInfo
import com.kappzzang.jeongsan.entity.MemberServiceIdResponse
import com.kappzzang.jeongsan.mapper.GroupEntityMapper.toGroupItem
import com.kappzzang.jeongsan.mapper.GroupEntityMapper.toServiceIdList
import org.junit.Assert.assertEquals
import org.junit.Test

class GroupEntityMapperTest {

    private fun getSampleGroupInfo() = GroupInfo(
        id = 123L,
        name = "Study Group",
        ownerServiceId = 0L,
        isCompleted = true,
        subject = "Math",
        previewList = listOf(
            MemberPreview(
                name = "Test Person 1",
                profileImageUrl = "http://test_profile_image1.url"
            ),
            MemberPreview(
                name = "Test Person 2",
                profileImageUrl = "http://test_profile_image2.url"
            )
        )
    )

    private fun getSampleMemberServiceIdResponse() = MemberServiceIdResponse(
        status = "success",
        message = "success",
        data = listOf(
            MemberServiceIdInfo(serviceId = "test_service_Id_1"),
            MemberServiceIdInfo(serviceId = "test_service_Id_2")
        )
    )

    @Test
    fun `GroupInfo 객체가 GroupItem 객체로 올바르게 매핑된다`() {
        // given
        val groupInfo = getSampleGroupInfo()

        // when
        val groupItem = groupInfo.toGroupItem()

        // then
        assertEquals("123", groupItem.id)
        assertEquals("Study Group", groupItem.name)
        assertEquals(true, groupItem.isCompleted)
        assertEquals("Math", groupItem.subject)
        assertEquals(2, groupItem.profileImageURL.size)
        assertEquals("http://test_profile_image1.url", groupItem.profileImageURL[0])
        assertEquals("http://test_profile_image2.url", groupItem.profileImageURL[1])
    }

    @Test
    fun `MemberServiceIdResponse 객체가 서비스 ID 리스트로 올바르게 매핑된다`() {
        // given
        val response = getSampleMemberServiceIdResponse()

        // when
        val serviceIdList = response.toServiceIdList()

        // then
        assertEquals(2, serviceIdList.size)
        assertEquals("test_service_Id_1", serviceIdList[0])
        assertEquals("test_service_Id_2", serviceIdList[1])
    }
}
