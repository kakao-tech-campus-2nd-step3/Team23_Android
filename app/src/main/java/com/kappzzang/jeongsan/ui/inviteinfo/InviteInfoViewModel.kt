package com.kappzzang.jeongsan.ui.inviteinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.domain.model.MemberItem
import com.kappzzang.jeongsan.domain.repository.MemberRepository
import com.kappzzang.jeongsan.domain.usecase.GetInviteInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InviteInfoViewModel @Inject constructor(
    private val getInviteInfoUseCase: GetInviteInfoUseCase
) : ViewModel() {
    private val _inviteInfo = MutableLiveData<List<MemberItem>>()
    val inviteInfo: LiveData<List<MemberItem>>
        get() = _inviteInfo

    init {
        // 더미 데이터 삽입
        viewModelScope.launch {
            getInviteInfoUseCase.insertDummyData()
        }
    }

    fun getInviteInfo() {
        viewModelScope.launch {
            _inviteInfo.postValue(getInviteInfoUseCase.invoke())
        }
    }
}