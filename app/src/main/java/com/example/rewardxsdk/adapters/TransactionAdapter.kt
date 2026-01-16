package com.example.rewardxsdk.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rewardxsdk.databinding.ItemExpenseBinding
import com.example.rewardxsdk.models.Transaction
import com.example.rewardxsdk.models.TransactionCategory
import com.example.rewardxsdk.models.TransactionType
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemExpenseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TransactionViewHolder(private val binding: ItemExpenseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())

        fun bind(transaction: Transaction) {
            binding.apply {
                tvIcon.text = TransactionCategory.getIcon(transaction.category, transaction.type)
                tvCategory.text = TransactionCategory.getDisplayName(transaction.category)
                tvDescription.text = transaction.description

                val amountText = if (transaction.type == TransactionType.INCOME) {
                    "+₪${String.format("%.2f", transaction.amount)}"
                } else {
                    "-₪${String.format("%.2f", transaction.amount)}"
                }
                tvAmount.text = amountText

                val amountColor = if (transaction.type == TransactionType.INCOME) {
                    itemView.context.getColor(com.example.rewardxsdk.R.color.success)  // #10B981
                } else {
                    itemView.context.getColor(com.example.rewardxsdk.R.color.error)    // #EF4444
                }
                tvAmount.setTextColor(amountColor)

                tvDate.text = dateFormat.format(Date(transaction.timestamp))
            }
        }
    }

    class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
    }
}