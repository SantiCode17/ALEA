package com.example.alea.ui.ranking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alea.data.model.User
import com.example.alea.databinding.ItemRankingBinding
import java.text.NumberFormat
import java.util.Locale

class RankingAdapter : ListAdapter<User, RankingAdapter.RankingViewHolder>(RankingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val binding = ItemRankingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RankingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        holder.bind(getItem(position), position + 4) // Start from 4th place
    }

    class RankingViewHolder(
        private val binding: ItemRankingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User, rank: Int) {
            binding.rankNumber.text = "#$rank"
            binding.playerName.text = user.username
            binding.pointsText.text = "${NumberFormat.getNumberInstance(Locale.US).format(user.coins)} pts"
        }
    }

    class RankingDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}
