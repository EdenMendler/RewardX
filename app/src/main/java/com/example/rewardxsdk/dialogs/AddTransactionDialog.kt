package com.example.rewardxsdk.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.example.rewardxsdk.R
import com.example.rewardxsdk.databinding.DialogAddTransactionBinding
import com.example.rewardxsdk.models.TransactionCategory
import com.example.rewardxsdk.models.TransactionType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddTransactionDialog(
    private val onAddExpense: (amount: Double, category: String, description: String) -> Unit,
    private val onAddIncome: (amount: Double, category: String, description: String) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: DialogAddTransactionBinding? = null
    private val binding get() = _binding!!

    private var selectedType = TransactionType.EXPENSE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTypeSelector()
        setupCategorySpinner()
        setupClickListeners()
    }

    private fun setupTypeSelector() {
        binding.chipGroupType.setOnCheckedStateChangeListener { _, checkedIds ->
            selectedType = if (checkedIds.contains(R.id.chip_expense)) {
                TransactionType.EXPENSE
            } else {
                TransactionType.INCOME
            }
            updateUIForType()
        }

        binding.chipExpense.isChecked = true
        updateUIForType()
    }

    private fun updateUIForType() {
        binding.tvTitle.text = if (selectedType == TransactionType.EXPENSE) {
            "Add Expense"
        } else {
            "Add Income"
        }

        val color = if (selectedType == TransactionType.EXPENSE) {
            ContextCompat.getColor(requireContext(), R.color.error)
        } else {
            ContextCompat.getColor(requireContext(), R.color.success)
        }
        binding.btnAdd.backgroundTintList = android.content.res.ColorStateList.valueOf(color)

        binding.btnAdd.text = if (selectedType == TransactionType.EXPENSE) {
            "Add Expense"
        } else {
            "Add Income"
        }
        updateCategoriesForType()
    }

    private fun setupCategorySpinner() {
        updateCategoriesForType()
    }

    private fun updateCategoriesForType() {
        val categories = if (selectedType == TransactionType.EXPENSE) {
            TransactionCategory.expenseCategories.map {
                "${TransactionCategory.getIcon(it, TransactionType.EXPENSE)} ${TransactionCategory.getDisplayName(it)}"
            }
        } else {
            TransactionCategory.incomeCategories.map {
                "${TransactionCategory.getIcon(it, TransactionType.INCOME)} ${TransactionCategory.getDisplayName(it)}"
            }
        }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            categories
        )
        binding.actvCategory.setAdapter(adapter)
        binding.actvCategory.setText(categories[0], false)
    }

    private fun setupClickListeners() {
        binding.btnAdd.setOnClickListener {
            val amountStr = binding.etAmount.text.toString().trim()
            val categoryText = binding.actvCategory.text.toString()
            val description = binding.etDescription.text.toString().trim()

            when {
                amountStr.isEmpty() -> {
                    binding.tilAmount.error = "Amount is required"
                }
                amountStr.toDoubleOrNull() == null || amountStr.toDouble() <= 0 -> {
                    binding.tilAmount.error = "Invalid amount"
                }
                description.isEmpty() -> {
                    binding.tilDescription.error = "Description is required"
                }
                else -> {
                    binding.tilAmount.error = null
                    binding.tilDescription.error = null

                    val amount = amountStr.toDouble()

                    val categories = if (selectedType == TransactionType.EXPENSE) {
                        TransactionCategory.expenseCategories
                    } else {
                        TransactionCategory.incomeCategories
                    }

                    val categoryIndex = categoryText.let { text ->
                        categories.indexOfFirst { category ->
                            text.contains(TransactionCategory.getDisplayName(category))
                        }.takeIf { it >= 0 } ?: 0
                    }
                    val category = categories[categoryIndex]

                    if (selectedType == TransactionType.EXPENSE) {
                        onAddExpense(amount, category, description)
                    } else {
                        onAddIncome(amount, category, description)
                    }

                    dismiss()
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}