package com.kappzzang.jeongsan.ui.expenselist

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kappzzang.jeongsan.R
import com.kappzzang.jeongsan.databinding.ActivityExpenseListBinding
import com.kappzzang.jeongsan.domain.model.OcrResultResponse
import com.kappzzang.jeongsan.ui.addexpense.AddExpenseActivity
import com.kappzzang.jeongsan.ui.camera.ReceiptCameraActivity
import com.kappzzang.jeongsan.ui.inviteinfo.InviteInfoActivity
import com.kappzzang.jeongsan.ui.sendmessage.SendMessageActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExpenseListActivity : AppCompatActivity() {
    private val viewModel: ExpenseListViewModel by viewModels()
    private lateinit var binding: ActivityExpenseListBinding
    private lateinit var navController: NavController
    private lateinit var activityReceiptCameraLauncher: ActivityResultLauncher<Intent>

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            startCameraActivity()
        } else {
            Toast.makeText(
                this,
                resources.getString(R.string.toast_message_deny),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.updateGroupId(intent.extras?.getString("groupId").toString())

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedExpense.collect {
                    if (it.isNotEmpty()) {
                        startExpenseDetailActivity(it)
                    }
                }
            }
        }

        val navHostFragment = supportFragmentManager.findFragmentById(
            binding.expenseListFragmentcontainerview.id
        ) as NavHostFragment
        navController = navHostFragment.navController

        setOnUpperMenuClickedListener()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.bottomnavigationview.setupWithNavController(navController)

        setOnAddExpenseFabClickedListener()


        activityReceiptCameraLauncher = createReceiptCameraLauncher()


        // TODO: 임시 연결용 코드
        binding.requestExpenseFab.setOnClickListener {
            startActivity(Intent(this, SendMessageActivity::class.java))
        }
    }

    private fun startAddExpenseActivity() {
        val intent = makeAddExpenseActivityIntent(true)
        startActivity(intent)
    }

    private fun startAddExpenseActivity(
        ocrResult: OcrResultResponse.OcrSuccess,
        receiptImage: Uri
    ) {
        val intent = makeAddExpenseActivityIntent(false)
        intent.putExtra(AddExpenseActivity.EXPENSE_DATA, ocrResult)
        intent.putExtra(AddExpenseActivity.EXPENSE_IMAGE, receiptImage)

        startActivity(intent)
    }

    private fun createReceiptCameraLauncher(): ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            val resultIntent: Intent? = it.data
            val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                resultIntent?.getParcelableExtra(
                    ReceiptCameraActivity.OCR_RESULT,
                    OcrResultResponse::class.java
                )
            } else {
                resultIntent?.getParcelableExtra(ReceiptCameraActivity.OCR_RESULT)
            }

            val image = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                resultIntent?.getParcelableExtra(
                    ReceiptCameraActivity.OCR_RESULT_IMAGE,
                    Uri::class.java
                )
            } else {
                resultIntent?.getParcelableExtra(ReceiptCameraActivity.OCR_RESULT_IMAGE)
            }

            if (it.resultCode == RESULT_OK) {
                if (data !is OcrResultResponse.OcrSuccess || image == null
                ) {
                    return@registerForActivityResult
                }
                startAddExpenseActivity(data, image)
            } else {
                (data as? OcrResultResponse.OcrFailed)?.message?.let { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun checkCameraPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) ==
                    PackageManager.PERMISSION_GRANTED
        } else true
    }

    private fun askCameraPermission() {
        if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
            // 권한 요청 이유를 설명하는 UI를 표시
            showNotificationPermissionDialog()
        } else {
            // Directly ask for the permission
            requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun showNotificationPermissionDialog() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.dialog_title_ask_camera))
            setMessage(
                String.format(
                    getString(R.string.dialog_body_ask_camera),
                    getString(R.string.app_name)
                )
            )
            setPositiveButton(getString(R.string.dialog_allow)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            setNegativeButton(getString(R.string.dialog_deny)) { _, _ -> }
            show()
        }
    }

    private fun makeAddExpenseActivityIntent(isManual: Boolean): Intent {
        val intent = Intent(
            this, AddExpenseActivity::class.java
        )

        intent.putExtra(
            AddExpenseActivity.INTENT_EXPENSE_MODE,
            if (isManual) AddExpenseActivity.EXPENSE_MODE_MANUAL else AddExpenseActivity.EXPENSE_MODE_RECEIPT
        )

        return intent
    }

    private fun startCameraActivity() {
        val intent = Intent(applicationContext, ReceiptCameraActivity::class.java)
        activityReceiptCameraLauncher.launch(intent)
    }

    private fun setOnAddExpenseFabClickedListener() {

        val popupMenu = PopupMenu(this, binding.addExpenseFab)
        popupMenu.menuInflater.inflate(R.menu.menu_add_expense, popupMenu.menu)
        popupMenu.setForceShowIcon(true)

        popupMenu.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener when (it.itemId) {
                R.id.menu_from_camera -> {
                    if (!checkCameraPermission()) {
                        askCameraPermission()
                    } else {
                        startCameraActivity()
                    }
                    true
                }

                R.id.menu_manually -> {
                    startAddExpenseActivity()
                    true
                }

                else -> false
            }
        }

        binding.addExpenseFab.setOnClickListener {
            popupMenu.show()
        }

    }

    private fun setOnUpperMenuClickedListener() {
        binding.dropdownButtonImageview.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.menu_group_setting, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                return@setOnMenuItemClickListener when (menuItem.itemId) {
                    R.id.menu_invite_status -> {
                        startActivity(Intent(this, InviteInfoActivity::class.java))
                        true
                    }

                    R.id.menu_end_group -> {
                        finish()
                        true
                    }

                    else -> {
                        false
                    }
                }
            }

            popupMenu.show()
        }
    }

    // TODO: 선택한 지출 확인용 임시 코드
    private fun startExpenseDetailActivity(expenseId: String) {
        Toast.makeText(this, expenseId, Toast.LENGTH_SHORT).show()
    }
}
