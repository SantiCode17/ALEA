package com.example.alea.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alea.data.model.Achievement
import com.example.alea.databinding.ItemAchievementBinding

class AchievementsAdapter(
    private val onAchievementClick: (Achievement) -> Unit = {}
) : ListAdapter<Achievement, AchievementsAdapter.AchievementViewHolder>(AchievementDiffCallback()) {

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

    inner class AchievementViewHolder(
        private val binding: ItemAchievementBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onAchievementClick(getItem(position))
                }
            }
        }

        fun bind(achievement: Achievement) {
            binding.apply {
                // Emoji
                emojiText.text = achievement.emoji

                // Name and description
                achievementName.text = achievement.name
                achievementDescription.text = achievement.description

                // Locked/Unlocked state
                if (achievement.isUnlocked) {
                    root.alpha = 1.0f
                    lockedOverlay.isVisible = false
                } else {
                    root.alpha = 0.5f
                    lockedOverlay.isVisible = true
                }

                // Rewards
                xpReward.text = "+${achievement.xpReward} XP"
                if (achievement.coinsReward > 0) {
                    coinsReward.text = "+${achievement.coinsReward}"
                    coinsReward.isVisible = true
                } else {
                    coinsReward.isVisible = false
                }
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
