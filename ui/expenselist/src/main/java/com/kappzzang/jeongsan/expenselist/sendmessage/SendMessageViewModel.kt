package com.kappzzang.jeongsan.expenselist.sendmessage

import androidx.lifecycle.ViewModel
import com.kappzzang.jeongsan.usecase.GetTransferInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SendMessageViewModel @Inject constructor(
    private val getTransferInfoUseCase: GetTransferInfoUseCase
) : ViewModel() {

}
