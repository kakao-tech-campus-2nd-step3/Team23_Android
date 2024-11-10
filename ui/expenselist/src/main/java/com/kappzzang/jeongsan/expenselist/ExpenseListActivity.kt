package com.kappzzang.jeongsan.expenselist

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
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
import com.kappzzang.jeongsan.expenselist.databinding.ActivityExpenseListBinding
import com.kappzzang.jeongsan.expenselist.inviteinfo.InviteInfoDialogFragment
import com.kappzzang.jeongsan.expenselist.viewmodel.CompleteGroupState
import com.kappzzang.jeongsan.expenselist.viewmodel.ExpenseListViewModel
import com.kappzzang.jeongsan.intentcontract.ExpenseListContract
import com.kappzzang.jeongsan.intentcontract.ReceiptCameraContract
import com.kappzzang.jeongsan.model.OcrResultResponse
import com.kappzzang.jeongsan.navigation.AddExpenseNavigator
import com.kappzzang.jeongsan.navigation.CameraNavigator
import com.kappzzang.jeongsan.navigation.ExpenseDetailNavigator
import com.kappzzang.jeongsan.navigation.SendMessageNavigator
import com.kappzzang.jeongsan.util.IntentHelper.getParcelableData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExpenseListActivity : AppCompatActivity() {
    @Inject
    lateinit var addExpenseNavigator: AddExpenseNavigator

    @Inject
    lateinit var cameraNavigator: CameraNavigator

    @Inject
    lateinit var sendMessageNavigator: SendMessageNavigator

    @Inject
    lateinit var expenseDetailNavigator: ExpenseDetailNavigator

    private val viewModel: ExpenseListViewModel by viewModels()
    private val binding: ActivityExpenseListBinding by lazy {
        val mBinding = ActivityExpenseListBinding.inflate(layoutInflater)
        mBinding.viewModel = viewModel
        mBinding.lifecycleOwner = this
        mBinding
    }
    private lateinit var navController: NavController
    private lateinit var activityReceiptCameraLauncher: ActivityResultLauncher<Intent>
    private val inviteInfoDialogFragment = InviteInfoDialogFragment()

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
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
        setContentView(binding.root)

        viewModel.updateGroupId(intent.extras?.getString(ExpenseListContract.GROUP_ID).toString())

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedExpense.collect {
                    if (it.expenseId.isNotEmpty()) {
                        viewModel.resetExpenseSelection()
                        startExpenseDetailActivity(it.expenseId, it.editable, it.isPayer)
                    }
                }
            }
        }

        initiateNavigation()
        setOnUpperMenuClickedListener()
        setOnAddExpenseFabClickedListener()
        collectCompleteGroupState()
        checkFromNewExpenseNotify()

        activityReceiptCameraLauncher = createReceiptCameraLauncher()

        binding.requestExpenseFab.setOnClickListener {
            navigateToSendMessage()
        }
    }

    private fun navigateToSendMessage() {
        val intent = sendMessageNavigator.navigateToSendMessage(this, viewModel.groupId.value)
        startActivity(intent)
    }

    private fun initiateNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(
            binding.expenseListFragmentcontainerview.id
        ) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomnavigationview.setupWithNavController(navController)
    }

    private fun setOnUpperMenuClickedListener() {
        binding.dropdownButtonImageview.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.menu_group_setting, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                return@setOnMenuItemClickListener when (menuItem.itemId) {
                    R.id.menu_invite_status -> {
                        inviteInfoDialogFragment.show(supportFragmentManager, "inviteInfoDialog")
                        true
                    }

                    R.id.menu_end_group -> {
                        viewModel.completeGroup()
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

    private fun collectCompleteGroupState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.completeGroupState.collect {
                    when (it) {
                        CompleteGroupState.IDLE -> {}
                        CompleteGroupState.SUCCESS -> {
                            Toast.makeText(
                                this@ExpenseListActivity,
                                "\"${viewModel.groupUIItem.value.groupSubject} " +
                                    "${viewModel.groupUIItem.value.groupName}\" " +
                                    getString(R.string.complete_group_success),
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }

                        CompleteGroupState.FAILED -> {
                            Toast.makeText(
                                this@ExpenseListActivity,
                                getString(R.string.complete_group_fail),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun startAddExpenseActivity() {
        val intent = addExpenseNavigator.navigateToAddExpenseManually(
            packageContext = this,
            groupId = viewModel.groupId.value
        )
        startActivity(intent)
    }

    private fun startAddExpenseActivity(
        ocrResult: OcrResultResponse.OcrSuccess,
        receiptImage: Uri
    ) {
        val intent =
            addExpenseNavigator.navigateToAddExpenseWithImage(
                packageContext = this,
                ocrResponse = ocrResult,
                image = receiptImage,
                groupId = viewModel.groupId.value
            )
        startActivity(intent)
    }

    private fun createReceiptCameraLauncher(): ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val resultIntent: Intent? = it.data
            val data =
                resultIntent?.getParcelableData<OcrResultResponse>(
                    ReceiptCameraContract.OCR_RESULT
                )
            val image = resultIntent?.getParcelableData<Uri>(
                ReceiptCameraContract.OCR_RESULT_IMAGE
            )

            if (it.resultCode == RESULT_OK) {
                if (data !is OcrResultResponse.OcrSuccess ||
                    image == null
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

    private fun checkCameraPermission(): Boolean = if (Build.VERSION.SDK_INT >=
        Build.VERSION_CODES.TIRAMISU
    ) {
        ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }

    private fun askCameraPermission() {
        if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
            // 권한 요청 이유를 설명하는 UI를 표시
            showCameraPermissionDialog()
        } else {
            // Directly ask for the permission
            requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun showCameraPermissionDialog() {
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

    private fun startCameraActivity() {
        val intent = cameraNavigator.navigateToCamera(applicationContext)
        activityReceiptCameraLauncher.launch(intent)
    }

    private fun startExpenseDetailActivity(
        expenseId: String,
        isEditable: Boolean,
        isPayer: Boolean
    ) {
        val groupId = viewModel.groupId.value
        val intent = expenseDetailNavigator.navigateToExpenseDetail(
            packageContext = this,
            groupId = groupId,
            expenseId = expenseId,
            editable = isEditable,
            isPayer = isPayer
        )
        startActivity(intent)
    }

    private fun checkFromNewExpenseNotify() {
        intent.extras?.getString(ExpenseListContract.EXPENSE_ID)?.let { expenseId ->
            intent.removeExtra(ExpenseListContract.EXPENSE_ID)
            // TODO: isEditable을 true, isPayer를 false로 설정 -> 수정해야하나..?
            startExpenseDetailActivity(expenseId, true, false)
        }
    }
}
