package com.kappzzang.jeongsan.addexpense

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.addexpense.colorpicker.ColorPickerDialog
import com.kappzzang.jeongsan.addexpense.data.ExpenseUploadUIState
import com.kappzzang.jeongsan.addexpense.databinding.ActivityAddExpenseBinding
import com.kappzzang.jeongsan.intentcontract.AddExpenseContract
import com.kappzzang.jeongsan.model.OcrResultResponse
import com.kappzzang.jeongsan.navigation.ExpenseDetailNavigator
import com.kappzzang.jeongsan.util.Base64BitmapEncoder
import com.kappzzang.jeongsan.util.IntentHelper.getParcelableData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddExpenseActivity : AppCompatActivity() {
    @Inject
    lateinit var appNavigator: ExpenseDetailNavigator
    private val colorPickerDialogFragment: ColorPickerDialog by lazy {
        createColorPickerDialogFragment()
    }
    private val viewModel: AddExpenseViewModel by viewModels()
    private val binding: ActivityAddExpenseBinding by lazy {
        ActivityAddExpenseBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        if (savedInstanceState == null) {
            initiateViewModel()
        }

        initiateRecyclerView()
        setContentView(binding.root)

        binding.addexpenseSubmitButton.setOnClickListener {
            if (!viewModel.uploadExpense()) {
                Toast.makeText(
                    this,
                    getString(R.string.add_expense_complete_form_notify),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.expenseImageBitmap.collect {
                    updateExpenseImage(it)
                }
            }
        }
        subscribeExpenseUploadState()
        binding.expenseSelectedCategory.setOnClickListener {
            showColorPickerDialog()
        }
    }

    private fun createColorPickerDialogFragment(): ColorPickerDialog {
        val dialog = ColorPickerDialog()
        dialog.dialog?.setCanceledOnTouchOutside(true)
        return dialog
    }

    private fun subscribeExpenseUploadState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uploadingProgress.collect { state ->
                    when (state) {
                        is ExpenseUploadUIState.Idle -> {
                            viewModel.setInputsLock(false)
                        }

                        is ExpenseUploadUIState.Uploading -> {
                            viewModel.setInputsLock(true)
                        }

                        is ExpenseUploadUIState.UploadSuccess -> {
                            startExpenseDetailActivityAndFinish(state.expenseId)
                        }

                        is ExpenseUploadUIState.UploadFailed -> {
                            viewModel.setInputsLock(false)
                        }
                    }
                }
            }
        }
    }

    private fun startExpenseDetailActivityAndFinish(uploadedExpenseId: String) {
        startActivity(
            appNavigator.navigateToExpenseDetail(
                packageContext = this,
                groupId = viewModel.groupId.value,
                expenseId = uploadedExpenseId,
                editable = true
            )
        )
        finish()
    }

    private fun showColorPickerDialog() {
        colorPickerDialogFragment.show(supportFragmentManager, "colorPickerDialog")
    }

    private fun updateExpenseImage(imageBitmap: Bitmap?) {
        if (imageBitmap == null) {
            return
        }

        binding.addexpenseImageImageview.setImageBitmap(imageBitmap)

        if (!checkIfReceiptMode()) {
            binding.addexpenseImageImageview.isVisible = true
            binding.addexpenseImagePlusImageview.isVisible = false
            binding.addexpenseImagePlusDescriptionTextview.isVisible = false
        }
    }

    private fun checkIfReceiptMode(): Boolean {
        val expenseMode = intent.extras?.getString(AddExpenseContract.INTENT_EXPENSE_MODE)

        return expenseMode == AddExpenseContract.EXPENSE_MODE_RECEIPT
    }

    private fun initiateViewModel() {
        getGroupId()
        if (checkIfReceiptMode()) {
            viewModel.setManualMode(AddExpenseViewModel.Companion.ManualMode.RECEIPT)
            getExpenseData()
        } else {
            viewModel.setManualMode(AddExpenseViewModel.Companion.ManualMode.MANUAL)
            setAddExpenseImageContainer()
        }
    }

    private fun setAddExpenseImageContainer() {
        binding.addexpenseImageContainer.setOnClickListener {
            openGalleryForImage()
        }
    }

    // ActivityResultLauncher for picking images from the gallery
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    val bitmap = Base64BitmapEncoder.convertUriToBitmap(uri, this)
                    viewModel.setExpenseImageBitmap(bitmap)
                }
            }
        }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
        pickImageLauncher.launch(intent)
    }

    private fun initiateRecyclerView() {
        with(binding.addexpenseItemListRecyclerview) {
            adapter = ExpenseItemListAdapter(viewModel::addNewExpense, viewModel::removeExpense)
            layoutManager =
                LinearLayoutManager(this@AddExpenseActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun getGroupId() {
        val groupId = intent?.getParcelableData<String>(
            AddExpenseContract.GROUP_ID
        )

        groupId?.let {
            viewModel.initGroupId(it)
        } ?: let {
            Toast.makeText(
                this,
                getString(R.string.add_expense_error_message_load_group_info),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getExpenseData() {
        val intentData = intent?.getParcelableData<OcrResultResponse>(
            AddExpenseContract.EXPENSE_DATA
        )
        val intentImage = intent?.getParcelableData<Uri>(AddExpenseContract.EXPENSE_IMAGE)

        val data =
            intentData as? OcrResultResponse.OcrSuccess ?: return
        val image = intentImage ?: return

        val bitmap = Base64BitmapEncoder.convertUriToBitmap(image, this)

        viewModel.setInitialReceiptData(bitmap, data)
    }
}
