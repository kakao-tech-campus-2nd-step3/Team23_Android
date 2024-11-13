package com.kappzzang.jeongsan.expenselist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kappzzang.jeongsan.data.ExpenseListUIState
import com.kappzzang.jeongsan.data.HasGroupId
import com.kappzzang.jeongsan.expenselist.databinding.ActivityExpenseListBinding
import com.kappzzang.jeongsan.expenselist.inviteinfo.InviteInfoDialogFragment
import com.kappzzang.jeongsan.expenselist.util.CameraPermissionHelper
import com.kappzzang.jeongsan.expenselist.viewmodel.ExpenseListViewModel
import com.kappzzang.jeongsan.intentcontract.ExpenseListContract
import com.kappzzang.jeongsan.intentcontract.ReceiptCameraContract
import com.kappzzang.jeongsan.model.ExpenseState
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

    private val cameraPermissionHelper = CameraPermissionHelper(
        requestCameraPermissionLauncher = requestCameraPermissionLauncher,
        context = this
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.updateGroupId(intent.extras?.getString(ExpenseListContract.GROUP_ID).toString())

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ExpenseListUIState.SelectingExpense -> {
                            if (state.selectedExpenseId.isNotEmpty()) {
                                viewModel.resetExpenseSelection()
                                startExpenseDetailActivity(
                                    state.selectedExpenseId,
                                    state.expenseState,
                                    state.isPayer
                                )
                            }
                        }

                        is ExpenseListUIState.CompleteSuccess -> {
                            Toast.makeText(
                                this@ExpenseListActivity,
                                "\"${state.groupSubject} " +
                                    "${state.groupName}\" " +
                                    getString(R.string.complete_group_success),
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }

                        is ExpenseListUIState.CompleteFailed -> {
                            Toast.makeText(
                                this@ExpenseListActivity,
                                getString(R.string.complete_group_fail),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {}
                    }
                }
            }
        }

        initiateNavigation()
        setOnUpperMenuClickedListener()
        setOnAddExpenseFabClickedListener()
        checkFromNewExpenseNotify()

        activityReceiptCameraLauncher = createReceiptCameraLauncher()

        binding.requestExpenseFab.setOnClickListener {
            navigateToSendMessage()
        }
    }

    private fun navigateToSendMessage() {
        val groupId = (viewModel.uiState.value as? HasGroupId)?.groupId ?: let {
            return
        }
        val intent = sendMessageNavigator.navigateToSendMessage(this, groupId)
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

                    else -> {
                        false
                    }
                }
            }

            popupMenu.show()
        }
    }

    private fun startAddExpenseActivity() {
        val groupId = (viewModel.uiState.value as? HasGroupId)?.groupId ?: let {
            return
        }
        val intent = addExpenseNavigator.navigateToAddExpenseManually(
            packageContext = this,
            groupId = groupId
        )
        startActivity(intent)
    }

    private fun startAddExpenseActivity(
        ocrResult: OcrResultResponse.OcrSuccess,
        receiptImage: Uri
    ) {
        val groupId = (viewModel.uiState.value as? HasGroupId)?.groupId ?: let {
            return
        }
        val intent =
            addExpenseNavigator.navigateToAddExpenseWithImage(
                packageContext = this,
                ocrResponse = ocrResult,
                image = receiptImage,
                groupId = groupId
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

    private fun setOnAddExpenseFabClickedListener() {
        val popupMenu = PopupMenu(this, binding.addExpenseFab)
        popupMenu.menuInflater.inflate(R.menu.menu_add_expense, popupMenu.menu)
        popupMenu.setForceShowIcon(true)

        popupMenu.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener when (it.itemId) {
                R.id.menu_from_camera -> {
                    cameraPermissionHelper.checkForPermissionAndRun(
                        shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)
                    ) {
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
        expenseState: ExpenseState,
        isPayer: Boolean
    ) {
        val groupId = (viewModel.uiState.value as? HasGroupId)?.groupId ?: let {
            return
        }
        val intent = expenseDetailNavigator.navigateToExpenseDetail(
            packageContext = this,
            groupId = groupId,
            expenseId = expenseId,
            expenseState = expenseState,
            isPayer = isPayer
        )
        startActivity(intent)
    }

    private fun checkFromNewExpenseNotify() {
        intent.extras?.getString(ExpenseListContract.EXPENSE_ID)?.let { expenseId ->
            intent.removeExtra(ExpenseListContract.EXPENSE_ID)
            startExpenseDetailActivity(expenseId, ExpenseState.NOT_CONFIRMED, false)
        }
    }
}
