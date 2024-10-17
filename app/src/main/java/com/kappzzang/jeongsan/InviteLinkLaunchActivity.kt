package com.kappzzang.jeongsan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class InviteLinkLaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.data?.getQueryParameter("invite_link")?.let {
            val inviteLinkIntent = Intent(Intent.ACTION_VIEW)
            inviteLinkIntent.data = Uri.parse(it)
            startActivity(inviteLinkIntent)
        }
        finish()
    }
}