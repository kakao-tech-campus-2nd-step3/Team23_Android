package com.kappzzang.jeongsan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class TransferLinkLaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.data?.getQueryParameter("transfer_link")?.let {
            val transferLinkIntent = Intent(Intent.ACTION_VIEW)
            transferLinkIntent.data = Uri.parse(it)
            startActivity(transferLinkIntent)
        }
        finish()
    }
}
