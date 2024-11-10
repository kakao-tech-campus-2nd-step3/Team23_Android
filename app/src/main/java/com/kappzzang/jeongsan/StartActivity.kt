package com.kappzzang.jeongsan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kappzzang.jeongsan.intentcontract.StartContract
import com.kappzzang.jeongsan.navigation.LoginNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {
    @Inject
    lateinit var appNavigator: LoginNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "stared")
        handleIntentData(intent)
    }

    private fun handleIntentData(intent: Intent?) {
        val transferLink = intent?.data?.getQueryParameter(StartContract.TRANSFER_LINK)
        val inviteGroup = intent?.data?.getQueryParameter(StartContract.INVITE_GROUP_ID)
        val newExpenseGroupId = intent?.data?.getQueryParameter(StartContract.NEW_EXPENSE_GROUP_ID)
        val newExpenseId = intent?.data?.getQueryParameter(StartContract.NEW_EXPENSE_ID)

        when {
            // 송금 링크를 클릭해서 옴
            transferLink != null -> {
                val transferLinkIntent = Intent(Intent.ACTION_VIEW)
                transferLinkIntent.data = Uri.parse(transferLink)
                Log.d(TAG, transferLink)
                startActivity(transferLinkIntent)
                finish()
            }
            // 초대링크를 클릭해서 옴
            inviteGroup != null -> {
                Log.d(TAG, inviteGroup)
                appNavigator.loginAndEnterGroup(this, inviteGroup).also {
                    startActivity(it)
                    finish()
                }
            }
            // 새 지출 등록 링크를 클릭해서 옴
            newExpenseGroupId != null && newExpenseId != null -> {
                Log.d(TAG, newExpenseGroupId)
                Log.d(TAG, newExpenseId)
                appNavigator.loginAndEnterDetailExpense(
                    this,
                    groupId = newExpenseGroupId,
                    expenseId = newExpenseId
                ).also {
                    startActivity(it)
                    finish()
                }
            }
            // 그냥 옴
            else -> {
                appNavigator.login(this).also {
                    startActivity(it)
                    finish()
                }
            }
        }
    }

    companion object {
        private const val TAG = "START_ACTIVITY"
    }
}
