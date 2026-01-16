package com.example.rewardxsdk.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rewardxsdk.models.Transaction
import com.example.rewardxsdk.models.TransactionType
import com.example.rewardxlibrary.manager.RewardXSDK
import com.example.rewardxlibrary.models.Achievement
import com.example.rewardxlibrary.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    private val _newAchievements = MutableStateFlow<List<Achievement>>(emptyList())
    val newAchievements: StateFlow<List<Achievement>> = _newAchievements.asStateFlow()

    private val _totalSpent = MutableStateFlow(0.0)
    val totalSpent: StateFlow<Double> = _totalSpent.asStateFlow()

    private val _totalIncome = MutableStateFlow(0.0)
    val totalIncome: StateFlow<Double> = _totalIncome.asStateFlow()

    private val _balance = MutableStateFlow(0.0)
    val balance: StateFlow<Double> = _balance.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadCurrentUser() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            RewardXSDK.getCurrentUser()
                .onSuccess { user ->
                    _user.value = user
                    loadUserAchievements()
                    loadTransactionsFromServer()
                }
                .onFailure { error ->
                    _error.value = error.message
                }

            _isLoading.value = false
        }
    }

    fun createUser(username: String, email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            RewardXSDK.createUser(username, email)
                .onSuccess { user ->
                    _user.value = user
                    loadUserAchievements()
                    loadTransactionsFromServer()
                }
                .onFailure { error ->
                    _error.value = error.message
                }

            _isLoading.value = false
        }
    }

    fun loginUser(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            RewardXSDK.getUserByEmail(email)
                .onSuccess { user ->
                    RewardXSDK.setUserId(user.id)
                    _user.value = user
                    loadUserAchievements()
                    loadTransactionsFromServer()
                }
                .onFailure { error ->
                    _error.value = error.message
                }

            _isLoading.value = false
        }
    }

    private fun loadTransactionsFromServer() {
        viewModelScope.launch {
            RewardXSDK.getUserEvents()
                .onSuccess { events ->
                    val transactionsFromServer = events.mapNotNull { event ->
                        try {
                            val amount = (event.eventData["amount"] as? Number)?.toDouble() ?: 0.0
                            val category = event.eventData["category"] as? String ?: "Other"
                            val description = event.eventData["description"] as? String ?: ""

                            val type = when (event.eventType) {
                                "expense_added" -> TransactionType.EXPENSE
                                "income_added" -> TransactionType.INCOME
                                else -> null
                            }

                            if (type != null) {
                                Transaction(
                                    id = event.id ?: UUID.randomUUID().toString(),
                                    amount = amount,
                                    category = category,
                                    description = description,
                                    type = type
                                )
                            } else {
                                null
                            }
                        } catch (e: Exception) {
                            null
                        }
                    }

                    // עדכון רשימת ה-transactions
                    _transactions.value = transactionsFromServer.sortedByDescending { it.id }

                    // חישוב סכומים
                    val expenses = transactionsFromServer.filter { it.type == TransactionType.EXPENSE }
                    val income = transactionsFromServer.filter { it.type == TransactionType.INCOME }

                    val totalExpenses = expenses.sumOf { it.amount }
                    val totalIncome = income.sumOf { it.amount }

                    _totalSpent.value = totalExpenses
                    _totalIncome.value = totalIncome
                    _balance.value = totalIncome - totalExpenses
                }
                .onFailure { error ->
                    _error.value = error.message
                }
        }
    }

    fun addExpense(amount: Double, category: String, description: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            // Create local transaction
            val transaction = Transaction(
                id = UUID.randomUUID().toString(),
                amount = amount,
                category = category,
                description = description,
                type = TransactionType.EXPENSE
            )
            _transactions.value = listOf(transaction) + _transactions.value

            // Update totals locally
            _totalSpent.value = _totalSpent.value + amount
            _balance.value = _totalIncome.value - _totalSpent.value

            // Track event
            val eventData = mapOf(
                "amount" to amount,
                "category" to category,
                "description" to description
            )

            RewardXSDK.trackEvent("expense_added", eventData)
                .onSuccess { newAchievements ->
                    if (newAchievements.isNotEmpty()) {
                        _newAchievements.value = newAchievements
                        loadCurrentUser() // Refresh user data
                        loadUserAchievements()
                    }
                }
                .onFailure { error ->
                    _error.value = error.message
                }

            _isLoading.value = false
        }
    }

    fun addIncome(amount: Double, category: String, description: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            // Create local transaction
            val transaction = Transaction(
                id = UUID.randomUUID().toString(),
                amount = amount,
                category = category,
                description = description,
                type = TransactionType.INCOME
            )
            _transactions.value = listOf(transaction) + _transactions.value

            // Update totals locally
            _totalIncome.value = _totalIncome.value + amount
            _balance.value = _totalIncome.value - _totalSpent.value

            // Track event
            val eventData = mapOf(
                "amount" to amount,
                "category" to category,
                "description" to description
            )

            RewardXSDK.trackEvent("income_added", eventData)
                .onSuccess { newAchievements ->
                    if (newAchievements.isNotEmpty()) {
                        _newAchievements.value = newAchievements
                        loadCurrentUser() // Refresh user data
                        loadUserAchievements()
                    }
                }
                .onFailure { error ->
                    _error.value = error.message
                }

            _isLoading.value = false
        }
    }

    fun loadUserAchievements() {
        viewModelScope.launch {
            RewardXSDK.getUserAchievements()
                .onSuccess { achievements ->
                    _achievements.value = achievements
                }
                .onFailure { error ->
                    _error.value = error.message
                }
        }
    }

    fun clearNewAchievements() {
        _newAchievements.value = emptyList()
    }

    fun clearError() {
        _error.value = null
    }

    fun getTotalSpent(): Double = _totalSpent.value

    fun getTotalIncome(): Double = _totalIncome.value

    fun getBalance(): Double = _balance.value

    fun getExpensesByCategory(): Map<String, Double> {
        return _transactions.value
            .filter { it.type == TransactionType.EXPENSE }
            .groupBy { it.category }
            .mapValues { it.value.sumOf { transaction -> transaction.amount } }
    }

    fun getIncomeByCategory(): Map<String, Double> {
        return _transactions.value
            .filter { it.type == TransactionType.INCOME }
            .groupBy { it.category }
            .mapValues { it.value.sumOf { transaction -> transaction.amount } }
    }
}