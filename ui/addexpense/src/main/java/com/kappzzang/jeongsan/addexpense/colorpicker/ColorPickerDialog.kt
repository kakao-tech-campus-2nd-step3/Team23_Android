package com.kappzzang.jeongsan.addexpense.colorpicker

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.addexpense.AddExpenseViewModel
import com.kappzzang.jeongsan.addexpense.databinding.DialogColorPickerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ColorPickerDialog : DialogFragment() {
    private val viewModel: ColorPickerViewModel by viewModels()
    private val activityViewModel: AddExpenseViewModel by activityViewModels()
    private lateinit var binding: DialogColorPickerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogColorPickerBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initiateRecyclerView()
        observeSelectedCategoryId()
        setDialogStyle()
        initiateConfirmButton()
    }

    private fun initiateConfirmButton() {
        binding.categoryListConfirmButton.setOnClickListener {
            dismiss()
        }
    }

    private fun initiateRecyclerView() {
        binding.categoryListRecyclerview.apply {
            adapter = CategoryListAdapter {
                activityViewModel.updateSelectedCategory(it)
            }
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setDialogStyle() {
        dialog?.window?.let { window ->
            window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

            val width = (resources.displayMetrics.widthPixels * 0.8).toInt()
            val height = (resources.displayMetrics.heightPixels * 0.7).toInt()
            window.setLayout(width, height)
        }
    }

    private fun observeSelectedCategoryId() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                activityViewModel.selectedCategory.collect {
                    viewModel.updateSelectedItem(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                activityViewModel.categoryList.collect {
                    viewModel.updateUIItemList(it, activityViewModel.selectedCategory.value.id)
                }
            }
        }
    }
}
