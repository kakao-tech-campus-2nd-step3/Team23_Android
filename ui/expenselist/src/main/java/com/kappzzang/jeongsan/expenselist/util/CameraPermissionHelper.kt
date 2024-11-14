package com.kappzzang.jeongsan.expenselist.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.kappzzang.jeongsan.expenselist.R

class CameraPermissionHelper(
    private val context: Context,
    private val requestCameraPermissionLauncher: ActivityResultLauncher<String>
) {

    fun checkForPermissionAndRun(dismissedBefore: Boolean, after: () -> Unit) {
        if (checkCameraPermission()) {
            after()
        } else {
            askCameraPermission(dismissedBefore)
        }
    }

    private fun checkCameraPermission(): Boolean = if (true
    ) {
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }

    private fun askCameraPermission(dismissedBefore: Boolean) {
        if (dismissedBefore) {
            // 권한 요청 이유를 설명하는 UI를 표시
            showCameraPermissionDialog()
        } else {
            // Directly ask for the permission
            requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun showCameraPermissionDialog() {
        AlertDialog.Builder(context).apply {
            setTitle(context.getString(R.string.dialog_title_ask_camera))
            setMessage(
                String.format(
                    context.getString(R.string.dialog_body_ask_camera),
                    context.getString(R.string.app_name)
                )
            )
            setPositiveButton(context.getString(R.string.dialog_allow)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            }
            setNegativeButton(context.getString(R.string.dialog_deny)) { _, _ -> }
            show()
        }
    }
}
