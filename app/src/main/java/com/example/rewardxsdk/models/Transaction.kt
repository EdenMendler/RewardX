package com.example.rewardxsdk.models

data class Transaction(
    val id: String,
    val amount: Double,
    val category: String,
    val description: String,
    val type: TransactionType,
    val timestamp: Long = System.currentTimeMillis()
)

enum class TransactionType {
    EXPENSE,
    INCOME
}

object TransactionCategory {
    // Expense Categories
    const val FOOD = "food"
    const val TRANSPORT = "transport"
    const val SHOPPING = "shopping"
    const val ENTERTAINMENT = "entertainment"
    const val BILLS = "bills"
    const val OTHER = "other"

    // Income Categories
    const val SALARY = "salary"
    const val BONUS = "bonus"
    const val FREELANCE = "freelance"
    const val GIFT = "gift"
    const val INVESTMENT = "investment"
    const val OTHER_INCOME = "other_income"

    val expenseCategories = listOf(FOOD, TRANSPORT, SHOPPING, ENTERTAINMENT, BILLS, OTHER)
    val incomeCategories = listOf(SALARY, BONUS, FREELANCE, GIFT, INVESTMENT, OTHER_INCOME)

    fun getIcon(category: String, type: TransactionType): String = when {
        type == TransactionType.EXPENSE -> when(category) {
            FOOD -> "🍔"
            TRANSPORT -> "🚗"
            SHOPPING -> "🛍️"
            ENTERTAINMENT -> "🎮"
            BILLS -> "📄"
            else -> "💰"
        }
        type == TransactionType.INCOME -> when(category) {
            SALARY -> "💼"
            BONUS -> "🎁"
            FREELANCE -> "💻"
            GIFT -> "🎉"
            INVESTMENT -> "📈"
            else -> "💵"
        }
        else -> "💰"
    }

    fun getDisplayName(category: String): String = when(category) {
        OTHER_INCOME -> "Other"
        else -> category.replaceFirstChar { it.uppercase() }
    }
}