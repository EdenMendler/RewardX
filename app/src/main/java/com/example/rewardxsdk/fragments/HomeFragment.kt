package com.example.rewardxsdk.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rewardxsdk.MainActivity
import com.example.rewardxsdk.R
import com.example.rewardxsdk.adapters.TransactionAdapter
import com.example.rewardxsdk.databinding.FragmentHomeBinding
import com.example.rewardxsdk.dialogs.AddTransactionDialog
import com.example.rewardxsdk.dialogs.AchievementUnlockedDialog
import com.example.rewardxsdk.dialogs.WelcomeDialog
import com.example.rewardxsdk.models.TransactionType
import com.example.rewardxsdk.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()

        viewModel.loadCurrentUser()
    }

    private fun setupRecyclerView() {
        transactionAdapter = TransactionAdapter()
        binding.recyclerExpenses.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = transactionAdapter
        }
    }

    private fun setupClickListeners() {
        binding.fabAddExpense.setOnClickListener {
            showAddTransactionDialog()
        }

        binding.cardProfile.setOnClickListener {
            // Navigate to profile using bottom navigation
            (activity as? MainActivity)?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                R.id.bottom_navigation
            )?.selectedItemId = R.id.nav_profile
        }

        binding.btnGetStarted.setOnClickListener {
            showWelcomeDialog()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.user.collect { user ->
                if (user != null) {
                    binding.layoutWelcome.visibility = View.GONE
                    binding.layoutMain.visibility = View.VISIBLE

                    binding.tvUsername.text = user.username
                    binding.tvPoints.text = "${user.totalPoints} pts"
                    binding.tvAchievementCount.text = "${user.achievements.size} 🏆"
                } else {
                    binding.layoutWelcome.visibility = View.VISIBLE
                    binding.layoutMain.visibility = View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.transactions.collect { transactions ->
                transactionAdapter.submitList(transactions)

                binding.layoutEmpty.visibility = if (transactions.isEmpty())
                    View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.balance.collect { balance ->
                binding.tvTotalSpent.text = "₪${String.format("%.2f", balance)}"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.totalIncome.collect { income ->
                // אפשר להוסיף TextView נוסף ל-layout אם רוצים להציג income בנפרד
                // binding.tvTotalIncome.text = "+₪${String.format("%.2f", income)}"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.totalSpent.collect { expenses ->
                // אפשר להוסיף TextView נוסף ל-layout אם רוצים להציג expenses בנפרד
                // binding.tvTotalExpenses.text = "-₪${String.format("%.2f", expenses)}"

                val transactionCount = viewModel.transactions.value.size
                binding.tvExpenseCount.text = "$transactionCount transactions"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.newAchievements.collect { achievements ->
                if (achievements.isNotEmpty()) {
                    showAchievementsDialog(achievements)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                    viewModel.clearError()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading)
                    View.VISIBLE else View.GONE
            }
        }
    }

    private fun showWelcomeDialog() {
        val dialog = WelcomeDialog(
            onCreateUser = { username, email ->
                viewModel.createUser(username, email)
            },
            onLoginUser = { userId ->
                viewModel.loginUser(userId)
            }
        )
        dialog.show(parentFragmentManager, "WelcomeDialog")
    }

    private fun showAddTransactionDialog() {
        val dialog = AddTransactionDialog(
            onAddExpense = { amount, category, description ->
                viewModel.addExpense(amount, category, description)
            },
            onAddIncome = { amount, category, description ->
                viewModel.addIncome(amount, category, description)
            }
        )
        dialog.show(parentFragmentManager, "AddTransactionDialog")
    }

    private fun showAchievementsDialog(achievements: List<com.example.rewardxlibrary.models.Achievement>) {
        val dialog = AchievementUnlockedDialog(achievements) {
            viewModel.clearNewAchievements()
        }
        dialog.show(parentFragmentManager, "AchievementDialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}