package com.kappzzang.jeongsan.main

import com.kappzzang.jeongsan.data.GroupViewItem
import com.kappzzang.jeongsan.model.GroupItem
import com.kappzzang.jeongsan.usecase.GetDoneGroupUseCase
import com.kappzzang.jeongsan.usecase.GetProgressingGroupUseCase
import com.kappzzang.jeongsan.usecase.GetUserInfoUseCase
import com.kappzzang.jeongsan.usecase.JoinGroupUseCase
import io.mockk.coEvery
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
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MainPageViewModelTest {

    private val mockGetProgressingGroupUseCase = mockk<GetProgressingGroupUseCase>()
    private val mockGetDoneGroupUseCase = mockk<GetDoneGroupUseCase>()
    private val mockGetUserInfoUseCase = mockk<GetUserInfoUseCase>()
    private val mockJoinGroupUseCase = mockk<JoinGroupUseCase>()
    private lateinit var viewModel: MainPageViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        coEvery { mockGetProgressingGroupUseCase() } returns emptyList()
        coEvery { mockGetDoneGroupUseCase() } returns emptyList()
        coEvery { mockGetUserInfoUseCase() } returns null
        viewModel = MainPageViewModel(
            mockGetProgressingGroupUseCase,
            mockGetDoneGroupUseCase,
            mockGetUserInfoUseCase,
            mockJoinGroupUseCase,
            testDispatcher
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `loadUserInfo가 정상적으로 정보를 가져오는지 확인`() = runTest {
        // Given
        val testUserName = "Test User"
        val testProfileUrl = "http://this.is.test.profile.url"
        coEvery { mockGetUserInfoUseCase() } returns mockk {
            every { name } returns testUserName
            every { profileUrl } returns testProfileUrl
        }

        // When
        viewModel.loadUserInfo()
        advanceUntilIdle()

        // Then
        assertEquals(testUserName, viewModel.userName.value)
        assertEquals(testProfileUrl, viewModel.userProfileUrl.value)
    }

    @Test
    fun `진행중인 그룹만 존재하는 경우 loadGroupList 함수 테스트`() = runTest {
        // Given
        val testProgressingGroupList = listOf<GroupItem>(mockk(), mockk())
        val testDoneGroupList = emptyList<GroupItem>()
        coEvery { mockGetProgressingGroupUseCase() } returns testProgressingGroupList
        coEvery { mockGetDoneGroupUseCase() } returns testDoneGroupList

        // When
        viewModel.loadGroupList()
        advanceUntilIdle()

        // Then
        assertEquals(testProgressingGroupList.size + 2, viewModel.groupList.value.size)
        assertEquals(GroupViewItem.ProgressTitle(false), viewModel.groupList.value[0])
        assertEquals(
            GroupViewItem.DoneTitle(true),
            viewModel.groupList.value[viewModel.groupList.value.size - 1]
        )
        for (i in testProgressingGroupList.indices) {
            assertEquals(
                GroupViewItem.Group(testProgressingGroupList[i]),
                viewModel.groupList.value[i + 1]
            )
        }
    }

    @Test
    fun `완료된 그룹만 존재하는 경우 loadGroupList 함수 테스트`() = runTest {
        // Given
        val testProgressingGroupList = emptyList<GroupItem>()
        val testDoneGroupList = listOf<GroupItem>(mockk(), mockk(), mockk())
        coEvery { mockGetProgressingGroupUseCase() } returns testProgressingGroupList
        coEvery { mockGetDoneGroupUseCase() } returns testDoneGroupList

        // When
        viewModel.loadGroupList()
        advanceUntilIdle()

        // Then
        assertEquals(testDoneGroupList.size + 2, viewModel.groupList.value.size)
        assertEquals(GroupViewItem.ProgressTitle(true), viewModel.groupList.value[0])
        assertEquals(GroupViewItem.DoneTitle(false), viewModel.groupList.value[1])
        for (i in testDoneGroupList.indices) {
            assertEquals(
                GroupViewItem.Group(testDoneGroupList[i]),
                viewModel.groupList.value[i + 2]
            )
        }
    }

    @Test
    fun `진행중인 그룹과 완료된 그룹이 모두 존재하는 경우 loadGroupList 함수 테스트`() = runTest {
        // Given
        val testProgressingGroupList = listOf<GroupItem>(mockk(), mockk())
        val testDoneGroupList = listOf<GroupItem>(mockk(), mockk(), mockk())
        coEvery { mockGetProgressingGroupUseCase() } returns testProgressingGroupList
        coEvery { mockGetDoneGroupUseCase() } returns testDoneGroupList

        // When
        viewModel.loadGroupList()
        advanceUntilIdle()

        // Then
        assertEquals(
            testProgressingGroupList.size + testDoneGroupList.size + 2,
            viewModel.groupList.value.size
        )
        assertEquals(GroupViewItem.ProgressTitle(false), viewModel.groupList.value[0])
        for (i in testProgressingGroupList.indices) {
            assertEquals(
                GroupViewItem.Group(testProgressingGroupList[i]),
                viewModel.groupList.value[i + 1]
            )
        }
        assertEquals(
            GroupViewItem.DoneTitle(false),
            viewModel.groupList.value[testProgressingGroupList.size + 1]
        )
        for (i in testDoneGroupList.indices) {
            assertEquals(
                GroupViewItem.Group(testDoneGroupList[i]),
                viewModel.groupList.value[testProgressingGroupList.size + i + 2]
            )
        }
    }
}
