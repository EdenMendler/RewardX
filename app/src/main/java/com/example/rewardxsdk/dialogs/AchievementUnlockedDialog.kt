package com.example.rewardxsdk.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rewardxsdk.adapters.AchievementAdapter
import com.example.rewardxsdk.databinding.DialogAchievementUnlockedBinding
import com.example.rewardxlibrary.models.Achievement

class AchievementUnlockedDialog(
    private val achievements: List<Achievement>,
    private val onDismiss: () -> Unit
) : DialogFragment() {

    private var _binding: DialogAchievementUnlockedBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAchievementUnlockedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val totalPoints = achievements.sumOf { it.points }

        binding.tvTitle.text = if (achievements.size == 1) {
            "🎉 Achievement Unlocked!"
        } else {
            "🎉 ${achievements.size} Achievements Unlocked!"
        }

        binding.tvPoints.text = "+$totalPoints pts"

        val adapter = AchievementAdapter()
        binding.recyclerAchievements.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
        adapter.submitList(achievements)

        binding.btnContinue.setOnClickListener {
            onDismiss()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}