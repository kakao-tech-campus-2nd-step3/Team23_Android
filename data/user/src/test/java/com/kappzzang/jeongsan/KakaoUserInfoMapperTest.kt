package com.kappzzang.jeongsan

import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.user.model.Account
import com.kakao.sdk.user.model.Profile
import com.kakao.sdk.user.model.User
import com.kappzzang.jeongsan.mapper.KakaoUserInfoMapper
import com.kappzzang.jeongsan.mapper.KakaoUserInfoMapper.toUserFriendItem
import org.junit.Assert.assertEquals
import org.junit.Test

class KakaoUserInfoMapperTest {

    @Test
    fun `User 객체가 UserItem 객체로 올바르게 매핑된다`() {
        // given
        val user = getSampleUser()

        // when
        val userItem = KakaoUserInfoMapper.mapKakaoUserModelToUserItem(user)

        // then
        assertEquals("12345", userItem.serviceId)
        assertEquals("Test User", userItem.name)
        assertEquals("this-is-test-email@test.email.com", userItem.email)
        assertEquals("http://test_profile_image.url", userItem.profileUrl)
    }

    @Test
    fun `서비스 아이디가 없는 경우, 빈 문자열로 매핑된다`() {
        // given
        val user = getSampleUser().copy(id = null)

        // when
        val userItem = KakaoUserInfoMapper.mapKakaoUserModelToUserItem(user)

        // then
        assertEquals("", userItem.serviceId)
    }

    @Test
    fun `Friend 객체가 UserFriendItem 객체로 올바르게 매핑된다`() {
        // given
        val friend = getSampleFriend()

        // when
        val userFriendItem = friend.toUserFriendItem()

        // then
        assertEquals("this_is_uuid", userFriendItem.uuid)
        assertEquals("1234", userFriendItem.serviceId)
    }

    private fun getSampleUser() = getEmptyUser().copy(
        id = 12345L,
        kakaoAccount = getEmptyAccount().copy(
            profile = Profile(
                nickname = "Test User",
                profileImageUrl = "http://test_profile_image.url",
                thumbnailImageUrl = null,
                isDefaultImage = null
            ),
            email = "this-is-test-email@test.email.com"
        )
    )

    private fun getSampleFriend() = Friend(
        id = 1234L,
        uuid = "this_is_uuid",
        profileNickname = null,
        profileThumbnailImage = null,
        favorite = null,
        allowedMsg = null
    )

    private fun getEmptyAccount() = Account(
        profileNeedsAgreement = null,
        profileNicknameNeedsAgreement = null,
        profileImageNeedsAgreement = null,
        profile = null,
        nameNeedsAgreement = null,
        name = null,
        emailNeedsAgreement = null,
        isEmailValid = null,
        isEmailVerified = null,
        email = null,
        ageRangeNeedsAgreement = null,
        ageRange = null,
        birthyearNeedsAgreement = null,
        birthyear = null,
        birthdayNeedsAgreement = null,
        birthday = null,
        birthdayType = null,
        genderNeedsAgreement = null,
        gender = null,
        legalNameNeedsAgreement = null,
        legalName = null,
        legalBirthDateNeedsAgreement = null,
        legalBirthDate = null,
        legalGenderNeedsAgreement = null,
        legalGender = null,
        phoneNumberNeedsAgreement = null,
        phoneNumber = null,
        isKoreanNeedsAgreement = null,
        isKorean = null
    )

    private fun getEmptyUser() = User(
        id = null,
        properties = null,
        kakaoAccount = null,
        groupUserToken = null,
        connectedAt = null,
        synchedAt = null,
        hasSignedUp = null,
        uuid = null
    )
}
