package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.entity.MemberInfo
import com.kappzzang.jeongsan.mapper.MemberEntityMapper.toMemberItem
import org.junit.Assert.assertEquals
import org.junit.Test

class MemberEntityMapperTest {

    private fun getSampleMemberInfo() = MemberInfo(
        id = "test_id",
        name = "Test Person",
        profileImageUrl = "http://test_profile_image1.url",
        isInvited = true
    )

    @Test
    fun `MemberInfo 객체가 MemberItem 객체로 올바르게 매핑된다`() {
        // given
        val memberInfo = getSampleMemberInfo()

        // when
        val memberItem = memberInfo.toMemberItem()

        // then
        assertEquals("test_id", memberItem.id)
        assertEquals("Test Person", memberItem.name)
        assertEquals("http://test_profile_image1.url", memberItem.profileImageUrl)
        assertEquals(true, memberItem.isInvited)
    }
}
