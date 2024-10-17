package com.kappzzang.jeongsan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
        // TODO: 임시로 바로 로그인 페이지로 이동
        // 이후 로그인 정보를 통해 바로 메인 페이지로 보내거나 결정하는 로직
        handleIntentData(intent)
    }
    private fun handleIntentData(intent: Intent?) {
        val transferLink = intent?.data?.getQueryParameter("transfer_link")
        val inviteLink = intent?.data?.getQueryParameter("invite_link")

        when {
            transferLink != null -> {
                val transferLinkIntent = Intent(Intent.ACTION_VIEW)
                transferLinkIntent.data = Uri.parse(transferLink)
                Log.d(TAG, transferLink)
                startActivity(transferLinkIntent)
                finish()
            }
            inviteLink != null -> {
                val inviteLinkIntent = Intent(Intent.ACTION_VIEW)
                inviteLinkIntent.data = Uri.parse(inviteLink)
                Log.d(TAG, inviteLink)
                appNavigator.navigateToLogin(this).also {
                    startActivity(it)
                    finish()
                }
            }
            else -> {
                appNavigator.navigateToLogin(this).also {
                    startActivity(it)
                    finish()
                }
            }
        }
    }

    companion object {
        private const val TAG = "StartActivity"
    }
}
