package com.example.alea.ui.challenge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alea.data.model.Friendship
import com.example.alea.databinding.ItemFriendSelectableBinding

class FriendSelectableAdapter(
    private val onFriendSelected: (Friendship) -> Unit
) : ListAdapter<Friendship, FriendSelectableAdapter.FriendViewHolder>(FriendDiffCallback()) {

    var selectedId: String? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding = ItemFriendSelectableBinding.inflate(
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
        private val binding: ItemFriendSelectableBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onFriendSelected(getItem(position))
                }
            }
            binding.selectCheckbox.setOnCheckedChangeListener { _, isChecked ->
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION && isChecked) {
                    onFriendSelected(getItem(position))
                }
            }
        }

        fun bind(friendship: Friendship) {
            binding.friendName.text = friendship.friendName
            binding.selectCheckbox.isChecked = friendship.friendId == selectedId
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
