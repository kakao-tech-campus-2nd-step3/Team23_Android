package com.kappzzang.jeongsan.expenselist.inviteinfo

import com.kappzzang.jeongsan.data.ExpenseListUIState
import com.kappzzang.jeongsan.usecase.ConvertServiceIdToUuidUseCase
import com.kappzzang.jeongsan.usecase.GetInviteInfoUseCase
import com.kappzzang.jeongsan.usecase.SendInviteMessageUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class InviteInfoViewModelTest {
    private val mockGetInviteInfoUseCase = mockk<GetInviteInfoUseCase>()
    private val mockSendInviteMessageUseCase = mockk<SendInviteMessageUseCase>(relaxed = true)
    private val mockConvertServiceIdToUuidUseCase = mockk<ConvertServiceIdToUuidUseCase>()
    private lateinit var viewModel: InviteInfoViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        coEvery { mockGetInviteInfoUseCase(any()) } returns emptyList()
        Dispatchers.setMain(testDispatcher)

        viewModel = InviteInfoViewModel(
            mockGetInviteInfoUseCase,
            mockSendInviteMessageUseCase,
            mockConvertServiceIdToUuidUseCase,
            testDispatcher
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `메시지 전송을 누르면 Uuid로 변환된 값으로 메시지 전송을 보낸다`() = runTest {
        // given
        val testGroupName = ""
        val testGroupId = "1"
        val testServiceId = "2"
        val testUuid = "22"

        coEvery { mockConvertServiceIdToUuidUseCase(any()) }.returns(listOf(testUuid))


        // when
        viewModel.sendInviteMessageWithServiceId(
            ExpenseListUIState.Idle(
                groupName = testGroupName,
                groupId = testGroupId,
                groupSubject = ""
            ),
            memberServiceId = testServiceId
        )
        advanceUntilIdle()

        // then
        coVerify {
            mockSendInviteMessageUseCase.invoke(
                groupId = testGroupId,
                groupName = testGroupName,
                memberUuidList = listOf(testUuid)
            )
        }
    }
}
