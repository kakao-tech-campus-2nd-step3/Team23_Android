package com.kappzzang.jeongsan

import com.kappzzang.jeongsan.model.TransferDetailItem
import com.kappzzang.jeongsan.model.UserItem
import com.kappzzang.jeongsan.repository.TransferRepository
import com.kappzzang.jeongsan.repository.UserInfoRepository
import com.kappzzang.jeongsan.usecase.SendTransferMessageUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class SendTransferMessageUseCaseTest {
    val mockTransferMessageRepository by lazy {
        mockk<TransferRepository>(relaxed = true)
    }
    val mockUserInfoRepository by lazy {
        mockk<UserInfoRepository>(relaxed = true)
    }

    lateinit var usecase: SendTransferMessageUseCase

    @Before
    fun setup() {
        coEvery { mockUserInfoRepository.getUserInfo() } returns sampleUserInfo
        coEvery { mockTransferMessageRepository.getTransferLink(any()) } returns sampleTransferLink
        coEvery {
            mockTransferMessageRepository.sendTransferMessage(
                any(),
                any(),
                any()
            )
        } returns true

        usecase = SendTransferMessageUseCase(
            mockUserInfoRepository,
            mockTransferMessageRepository
        )
    }

    @Test
    fun `유저 정보가 잘못된 경우 False를 리턴한다`() {
        // given
        coEvery { mockUserInfoRepository.getUserInfo() } returns null
        val input = sampleTransferList

        val result: Boolean
        // when
        runBlocking {
            result = usecase.invoke(input)
        }

        // then
        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `송금 링크가 Null인 경우 False를 리턴한다`() {
        // given
        coEvery { mockTransferMessageRepository.getTransferLink(any()) } returns null
        val input = sampleTransferList

        val result: Boolean

        // when
        runBlocking {
            result = usecase.invoke(input)
        }

        // then
        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `이상 없이 실행되면 True를 리턴한다`() {
        // given
        val input = sampleTransferList

        val result: Boolean

        // when
        runBlocking {
            result = usecase.invoke(input)
        }

        // then
        assertThat(result).isEqualTo(true)
    }

    companion object {
        val sampleTransferList =
            listOf(
                TransferDetailItem(
                    "sampleItem",
                    "name",
                    100,
                    "https://example.org/"
                )
            )

        val sampleUserInfo =
            UserItem(
                name = "sampleUser",
                uuid = "1234",
                profileUrl = "https://example.org/"
            )

        val sampleTransferLink =
            "sampleLink"
    }
}
