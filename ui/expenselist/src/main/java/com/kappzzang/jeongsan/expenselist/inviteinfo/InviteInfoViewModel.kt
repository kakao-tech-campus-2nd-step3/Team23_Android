package com.kappzzang.jeongsan.expenselist.inviteinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.model.MemberItem
import com.kappzzang.jeongsan.usecase.GetInviteInfoUseCase
import com.kappzzang.jeongsan.usecase.SendInviteMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class InviteInfoViewModel @Inject constructor(
    private val getInviteInfoUseCase: GetInviteInfoUseCase,
    private val sendInviteMessageUseCase: SendInviteMessageUseCase
) : ViewModel() {

    private val _inviteInfo = MutableStateFlow<List<MemberItem>>(emptyList())
    val inviteInfo: StateFlow<List<MemberItem>> = _inviteInfo

    init {
        // 더미 데이터 삽입 & 적용
        viewModelScope.launch(Dispatchers.IO) {
            getInviteInfoUseCase.insertDummyData()
            _inviteInfo.emit(getInviteInfoUseCase())
        }
    }

    fun sendInviteMessage(groupId: String, groupName: String, memberId: String) =
        viewModelScope.launch {
            sendInviteMessageUseCase.invoke(groupId, groupName, memberId)
        }
}
