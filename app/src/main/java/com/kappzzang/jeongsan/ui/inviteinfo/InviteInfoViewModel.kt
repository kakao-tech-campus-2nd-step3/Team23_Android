package com.kappzzang.jeongsan.ui.inviteinfo

import androidx.lifecycle.ViewModel
import com.kappzzang.jeongsan.domain.repository.MemberRepository
import com.kappzzang.jeongsan.domain.usecase.GetInviteInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InviteInfoViewModel @Inject constructor(
    private val getInviteInfoUseCase: GetInviteInfoUseCase
//    private val memberRepository: MemberRepository
) : ViewModel() {
    fun getMember() {
    }
}