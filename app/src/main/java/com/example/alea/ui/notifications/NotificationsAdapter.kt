package com.example.alea.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alea.R
import com.example.alea.data.model.Notification
import com.example.alea.data.model.NotificationType
import com.example.alea.databinding.ItemNotificationBinding

class NotificationsAdapter(
    private val onNotificationClick: (Notification) -> Unit,
    private val onNotificationDismiss: (Notification) -> Unit
) : ListAdapter<Notification, NotificationsAdapter.NotificationViewHolder>(NotificationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NotificationViewHolder(
        private val binding: ItemNotificationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onNotificationClick(getItem(position))
                }
            }

            binding.dismissButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onNotificationDismiss(getItem(position))
                }
            }
        }

        fun bind(notification: Notification) {
            binding.apply {
                // Set emoji icon
                emojiIcon.text = notification.emoji

                // Set title and message
                notificationTitle.text = notification.title
                notificationMessage.text = notification.message

                // Set time
                timeText.text = notification.getRelativeTime()

                // Set read/unread indicator
                unreadIndicator.visibility = if (notification.isRead) View.GONE else View.VISIBLE

                // Set background based on read state
                root.alpha = if (notification.isRead) 0.7f else 1.0f

                // Set icon background color based on type
                val iconBgColor = when (notification.type) {
                    NotificationType.CHALLENGE_RECEIVED,
                    NotificationType.CHALLENGE_ACCEPTED -> R.color.alea_primary
                    NotificationType.CHALLENGE_WON -> R.color.alea_success
                    NotificationType.CHALLENGE_LOST -> R.color.alea_error
                    NotificationType.FRIEND_REQUEST,
                    NotificationType.FRIEND_ACCEPTED -> R.color.alea_secondary
                    NotificationType.ACHIEVEMENT_UNLOCKED -> R.color.alea_coin
                    NotificationType.LEVEL_UP -> R.color.alea_primary_variant
                    NotificationType.CHALLENGE_COMPLETED -> R.color.alea_info
                    NotificationType.SYSTEM -> R.color.alea_surface_variant
                }
                iconBackground.setCardBackgroundColor(
                    ContextCompat.getColor(root.context, iconBgColor)
                )
            }
        }
    }

    class NotificationDiffCallback : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }
    }
}
