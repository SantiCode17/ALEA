package com.example.alea.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alea.R
import com.example.alea.data.model.Challenge
import com.example.alea.data.model.ChallengeStatus
import com.example.alea.databinding.ItemChallengeCardBinding

class ChallengesAdapter(
    private val onChallengeClick: (Challenge) -> Unit
) : ListAdapter<Challenge, ChallengesAdapter.ChallengeViewHolder>(ChallengeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val binding = ItemChallengeCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChallengeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChallengeViewHolder(
        private val binding: ItemChallengeCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onChallengeClick(getItem(position))
                }
            }
        }

        fun bind(challenge: Challenge) {
            // Show category emoji + title
            binding.challengeTitle.text = "${challenge.category.emoji} ${challenge.title}"
            binding.betAmount.text = challenge.betAmount.toString()

            // Show opponent name if available
            if (challenge.opponentName.isNotEmpty()) {
                binding.opponentName.text = "vs ${challenge.opponentName}"
                binding.opponentName.visibility = android.view.View.VISIBLE
            } else {
                binding.opponentName.visibility = android.view.View.GONE
            }

            val (statusText, statusBackground) = when (challenge.status) {
                ChallengeStatus.ACTIVE -> "Active" to R.drawable.bg_badge_active
                ChallengeStatus.COMPLETED -> {
                    if (challenge.result.name == "WON") "Won" to R.drawable.bg_badge_won
                    else "Lost" to R.drawable.bg_badge_lost
                }
                ChallengeStatus.PENDING -> "Pending" to R.drawable.bg_badge_active
                ChallengeStatus.CANCELLED -> "Cancelled" to R.drawable.bg_badge_lost
            }
            binding.statusBadge.text = statusText
            binding.statusBadge.setBackgroundResource(statusBackground)
        }
    }

    class ChallengeDiffCallback : DiffUtil.ItemCallback<Challenge>() {
        override fun areItemsTheSame(oldItem: Challenge, newItem: Challenge): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Challenge, newItem: Challenge): Boolean {
            return oldItem == newItem
        }
    }
}
