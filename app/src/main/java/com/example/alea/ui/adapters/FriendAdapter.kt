package com.example.alea.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.alea.R
import com.example.alea.data.model.Friend

class FriendAdapter(
    private val items: List<Friend>,
    private val onChatClick: (Friend) -> Unit,
    private val onItemClick: (Friend) -> Unit
) : RecyclerView.Adapter<FriendAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.friend_name)
        val username: TextView = view.findViewById(R.id.friend_username)
        val onlineIndicator: View = view.findViewById(R.id.friend_online_indicator)
        val chatBtn: ImageView = view.findViewById(R.id.friend_chat_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.username.text = item.username

        holder.onlineIndicator.visibility = if (item.isOnline) View.VISIBLE else View.GONE
        holder.chatBtn.setOnClickListener { onChatClick(item) }
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount() = items.size
}
