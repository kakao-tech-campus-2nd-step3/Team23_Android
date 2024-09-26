package com.kappzzang.jeongsan.ui.addexpense

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
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
import com.kappzzang.jeongsan.domain.model.OcrResultResponse
import com.kappzzang.jeongsan.ui.expensedetail.ExpenseDetailActivity
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
            viewModel.initiateDemoData()
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
                    val bitmap = convertImageUriToBitmap(uri)
                    viewModel.setExpenseImageBitmap(bitmap)
                }
            }
        }

    private fun convertImageUriToBitmap(uri: Uri): Bitmap = if (Build.VERSION.SDK_INT >= 28) {
        val source = ImageDecoder.createSource(contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    } else {
        MediaStore.Images.Media.getBitmap(contentResolver, uri)
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
        val intentData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(
                EXPENSE_DATA,
                OcrResultResponse::class.java
            )
        } else {
            intent?.getParcelableExtra(EXPENSE_DATA)
        }

        val intentImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(
                EXPENSE_IMAGE,
                Uri::class.java
            )
        } else {
            intent?.getParcelableExtra(EXPENSE_IMAGE)
        }

        val data = intentData as? OcrResultResponse.OcrSuccess ?: return
        val image = intentImage as? Uri ?: return

        // TODO: data를 사용해 Expense의 나머지 필드 초기화
    }

    companion object {
        const val INTENT_EXPENSE_MODE = "expenseMode"
        const val EXPENSE_MODE_MANUAL = "manual"
        const val EXPENSE_MODE_RECEIPT = "receipt"

        const val EXPENSE_DATA = "expenseData"
        const val EXPENSE_IMAGE = "expenseImage"
    }
}
