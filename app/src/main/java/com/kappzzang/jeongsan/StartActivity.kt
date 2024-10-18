package com.kappzzang.jeongsan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kappzzang.jeongsan.intentcontract.ReceiptCameraContract
import com.kappzzang.jeongsan.intentcontract.StartContract
import com.kappzzang.jeongsan.navigation.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {
    @Inject
    lateinit var appNavigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "stared")
        handleIntentData(intent)
    }
    private fun handleIntentData(intent: Intent?) {
        val transferLink = intent?.data?.getQueryParameter(StartContract.TRANSFER_LINK)
        val inviteGroup = intent?.data?.getQueryParameter(StartContract.INVITE_GROUP_ID)

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
                appNavigator.navigateToLogin(this).also {
                    it.data = Uri.parse(inviteGroup)
                    startActivity(it)
                    finish()
                }
            }
            // 그냥 옴
            else -> {
                appNavigator.navigateToLogin(this).also {
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
