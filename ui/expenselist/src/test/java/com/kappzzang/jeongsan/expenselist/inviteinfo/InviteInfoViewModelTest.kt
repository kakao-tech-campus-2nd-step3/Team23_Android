package com.kappzzang.jeongsan.expenselist.inviteinfo

import com.kappzzang.jeongsan.usecase.GetInviteInfoUseCase
import com.kappzzang.jeongsan.usecase.SendInviteMessageUseCase
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class InviteInfoViewModelTest {
    private val getInviteInfoUseCase = mockk<GetInviteInfoUseCase>()
    private val sendInviteMessageUseCase = mockk<SendInviteMessageUseCase>()
    private lateinit var viewModel: InviteInfoViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        coEvery { getInviteInfoUseCase() } returns emptyList()
        coEvery { getInviteInfoUseCase.insertDummyData() } returns Unit
        coEvery { sendInviteMessageUseCase(any(), any(), any()) }
        Dispatchers.setMain(testDispatcher)

        viewModel = InviteInfoViewModel(
            getInviteInfoUseCase,
            sendInviteMessageUseCase,
            testDispatcher
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `메시지 전송에 대한 테스트`() = runTest {
        // 테스트할 내용이 없는것 같아 미구현
    }
}
