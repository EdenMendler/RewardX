package com.example.rewardxsdk.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rewardxsdk.adapters.AchievementAdapter
import com.example.rewardxsdk.databinding.FragmentProfileBinding
import com.example.rewardxsdk.viewmodels.MainViewModel
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var achievementAdapter: AchievementAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        viewModel.loadCurrentUser()
        viewModel.loadUserAchievements()
    }

    private fun setupRecyclerView() {
        achievementAdapter = AchievementAdapter()
        binding.recyclerAchievements.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = achievementAdapter
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.user.collect { user ->
                user?.let {
                    binding.tvUsername.text = it.username
                    binding.tvEmail.text = it.email
                    binding.tvTotalPoints.text = it.totalPoints.toString()
                    binding.tvAchievementCount.text = it.achievements.size.toString()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.transactions.collect { transactions ->
                binding.tvExpenseCount.text = transactions.size.toString()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.balance.collect { balance ->
                binding.tvTotalSpent.text = "₪${String.format("%.2f", balance)}"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.totalIncome.collect { income ->
                // אם יש binding.tvTotalIncome ב-layout:
                // binding.tvTotalIncome.text = "+₪${String.format("%.2f", income)}"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.totalSpent.collect { expenses ->
                // אם יש binding.tvTotalExpenses ב-layout:
                // binding.tvTotalExpenses.text = "-₪${String.format("%.2f", expenses)}"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.achievements.collect { achievements ->
                achievementAdapter.submitList(achievements)

                binding.layoutEmpty.visibility = if (achievements.isEmpty())
                    View.VISIBLE else View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}