package com.example.alea.ui.friends

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alea.R
import com.example.alea.data.model.Friendship
import com.example.alea.databinding.ItemFriendBinding

class FriendsAdapter(
    private val onFriendClick: (Friendship) -> Unit,
    private val onMessageClick: (Friendship) -> Unit
) : ListAdapter<Friendship, FriendsAdapter.FriendViewHolder>(FriendDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding = ItemFriendBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FriendViewHolder(
        private val binding: ItemFriendBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onFriendClick(getItem(position))
                }
            }
            binding.messageButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onMessageClick(getItem(position))
                }
            }
        }

        fun bind(friendship: Friendship) {
            binding.friendName.text = friendship.friendName

            // For now, show as online (would need real-time status)
            val isOnline = true // Placeholder
            binding.onlineIndicator.setBackgroundResource(
                if (isOnline) R.drawable.indicator_online else R.drawable.indicator_offline
            )
            binding.statusText.text = if (isOnline) "Online" else "Offline"
        }
    }

    class FriendDiffCallback : DiffUtil.ItemCallback<Friendship>() {
        override fun areItemsTheSame(oldItem: Friendship, newItem: Friendship): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Friendship, newItem: Friendship): Boolean {
            return oldItem == newItem
        }
    }
}
