package com.example.rewardxsdk.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rewardxsdk.databinding.ItemAchievementBinding
import com.example.rewardxlibrary.models.Achievement

class AchievementAdapter : ListAdapter<Achievement, AchievementAdapter.AchievementViewHolder>(
    AchievementDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val binding = ItemAchievementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AchievementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AchievementViewHolder(private val binding: ItemAchievementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(achievement: Achievement) {
            binding.apply {
                tvIcon.text = achievement.icon
                tvName.text = achievement.name
                tvDescription.text = achievement.description
                tvPoints.text = "+${achievement.points} pts"
            }
        }
    }

    class AchievementDiffCallback : DiffUtil.ItemCallback<Achievement>() {
        override fun areItemsTheSame(oldItem: Achievement, newItem: Achievement): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Achievement, newItem: Achievement): Boolean {
            return oldItem == newItem
        }
    }
}