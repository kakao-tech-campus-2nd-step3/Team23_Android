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
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kappzzang.jeongsan.databinding.ActivityAddExpenseBinding
import com.kappzzang.jeongsan.model.OcrResultResponse
import com.kappzzang.jeongsan.ui.expensedetail.ExpenseDetailActivity
import com.example.androidutil.Base64BitmapEncoder
import com.example.androidutil.IntentHelper.getParcelableData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@BindingAdapter("app:items")
fun attachList(recyclerView: RecyclerView, items: StateFlow<List<ExpenseItemInput>>?) {
    items?.let {
        (recyclerView.adapter as? ExpenseItemListAdapter)?.submitList(it.value)
    }
}

@AndroidEntryPoint
class AddExpenseActivity : AppCompatActivity() {
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

        // TODO: 임시 연결용 코드
        binding.addexpenseSubmitButton.setOnClickListener {
            if (viewModel.uploadExpense()) {
                startActivity(Intent(this, ExpenseDetailActivity::class.java))
                finish()
                return@setOnClickListener
            }

            // TODO: 값이 완전히 채워지지 않은 경우
            Toast.makeText(this, "지출 내역을 완성해주세요!", Toast.LENGTH_SHORT).show()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.expenseImageBitmap.collect {
                    updateExpenseImage(it)
                }
            }
        }
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
        val expenseMode = intent.extras?.getString(INTENT_EXPENSE_MODE)

        return expenseMode == EXPENSE_MODE_RECEIPT
    }

    private fun initiateViewModel() {
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
                    val bitmap = com.example.androidutil.Base64BitmapEncoder.convertUriToBitmap(uri, this)
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

    private fun getExpenseData() {
        val intentData = intent?.getParcelableData<com.kappzzang.jeongsan.model.OcrResultResponse>(
            EXPENSE_DATA
        )
        val intentImage = intent?.getParcelableData<Uri>(EXPENSE_IMAGE)

        val data = intentData as? com.kappzzang.jeongsan.model.OcrResultResponse.OcrSuccess ?: return
        val image = intentImage ?: return

        val bitmap = com.example.androidutil.Base64BitmapEncoder.convertUriToBitmap(image, this)

        viewModel.setInitialReceiptData(bitmap, data)
    }

    companion object {
        const val INTENT_EXPENSE_MODE = "expenseMode"
        const val EXPENSE_MODE_MANUAL = "manual"
        const val EXPENSE_MODE_RECEIPT = "receipt"

        const val EXPENSE_DATA = "expenseData"
        const val EXPENSE_IMAGE = "expenseImage"
    }
}
