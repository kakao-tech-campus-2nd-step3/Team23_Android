package com.kappzzang.jeongsan.creategroup

import android.app.Application
import com.kappzzang.jeongsan.data.MemberUIData
import com.kappzzang.jeongsan.usecase.SendInviteMessageUseCase
import com.kappzzang.jeongsan.usecase.UploadGroupInfoUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CreateGroupViewModelTest {

    private val mockApplication = mockk<Application>()
    private val mockUploadGroupInfoUseCase = mockk<UploadGroupInfoUseCase>()
    private val mockSendInviteMessageUseCase = mockk<SendInviteMessageUseCase>()
    private lateinit var viewModel: CreateGroupViewModel

    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { mockUploadGroupInfoUseCase(any()) } returns 1L
        coEvery { mockSendInviteMessageUseCase(any(), any(), any()) } returns true
        viewModel = CreateGroupViewModel(
            mockApplication,
            mockUploadGroupInfoUseCase,
            mockSendInviteMessageUseCase,
            testDispatcher
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `그룹 주제(이모지) 업데이트시 올바르게 반영하는지 확인`() = runTest {
        // given
        val testSubject = "😊"

        // when
        viewModel.updateGroupSubject(testSubject)
        advanceUntilIdle()

        // then
        assertEquals(testSubject, viewModel.groupSubject.value)
    }

    @Test
    fun `그룹 멤버 리스트 업데이트시 올바르게 반영되는지 확인`() = runTest {
        // given
        val testMembers = listOf(
            MemberUIData("1", "12", "Alice", "test url1"),
            MemberUIData("2", "22", "Bob", "test url2")
        )

        // when
        viewModel.updateGroupMemberList(testMembers)
        advanceUntilIdle()

        // then
        assertEquals(testMembers, viewModel.groupMemberList.value)
    }

    @Test
    fun `멤버 제거시 해당 위치의 멤버가 삭제되는지 확인`() = runTest {
        // given
        val testMembers = listOf(
            MemberUIData("1", "12", "Alice", "test url1"),
            MemberUIData("2", "22", "Bob", "test url2"),
            MemberUIData("3", "32", "Charlie", "test url3")
        )
        viewModel.updateGroupMemberList(testMembers)
        advanceUntilIdle()

        // when
        viewModel.removeMember(1) // "Bob" 제거
        advanceUntilIdle()

        // then
        val updatedList = viewModel.groupMemberList.value
        assertEquals(testMembers.size - 1, updatedList.size)
        assertEquals(testMembers[0].name, updatedList[0].name)
        assertEquals(testMembers[2].name, updatedList[1].name)
    }

    @Test
    fun `그룹 정보가 유효할때 확인`() = runTest {
        // given
        val testGroupName = "Test Group"
        val testGroupSubject = "✈️"
        val testMembers = listOf(
            MemberUIData("1", "12", "Alice", "test url1"),
            MemberUIData("2", "22", "Bob", "test url2")
        )
        viewModel.groupName.emit(testGroupName)
        viewModel.updateGroupSubject(testGroupSubject)
        viewModel.updateGroupMemberList(testMembers)
        advanceUntilIdle()

        // when
        val result = viewModel.checkGroupInfoValidation()
        advanceUntilIdle()

        // then
        assertEquals(true, result)
    }

    @Test
    fun `모든 멤버에게 초대 메시지를 전송하는지 확인`() = runTest {
        // given
        val testGroupId = "test_group_id"
        val testGroupName = "Test Group"
        val testMembers = listOf(
            MemberUIData("1", "12", "Alice", "test url1"),
            MemberUIData("2", "22", "Bob", "test url2")
        )
        viewModel.groupName.emit(testGroupName)
        viewModel.updateGroupMemberList(testMembers)
        advanceUntilIdle()

        // when
        viewModel.sendInviteMessageAll(testGroupId)
        advanceUntilIdle()

        // then
        coVerify {
            mockSendInviteMessageUseCase(testGroupId, testGroupName, testMembers.map { it.uuid })
        }
    }
}
