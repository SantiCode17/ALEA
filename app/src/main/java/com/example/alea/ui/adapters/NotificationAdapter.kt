package com.example.alea.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alea.R
import com.example.alea.data.model.Notification

class NotificationAdapter(
    private val items: MutableList<Notification>,
    private val onItemClick: ((Notification) -> Unit)? = null,
    private val onDeleteClick: ((Int) -> Unit)? = null
) : RecyclerView.Adapter<NotificationAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.notification_icon)
        val title: TextView = view.findViewById(R.id.notification_title)
        val message: TextView = view.findViewById(R.id.notification_message)
        val time: TextView = view.findViewById(R.id.notification_time)
        val unreadDot: View = view.findViewById(R.id.notification_unread_dot)
        val deleteBtn: ImageView = view.findViewById(R.id.notification_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.title.text = if (item.senderName.isNotEmpty()) item.senderName else "ALEA"
        holder.message.text = item.text
        holder.time.text = item.time

        // Unread dot indicator
        if (!item.isRead) {
            holder.unreadDot.visibility = View.VISIBLE
            holder.itemView.alpha = 1f
            holder.deleteBtn.visibility = View.GONE
        } else {
            holder.unreadDot.visibility = View.GONE
            holder.itemView.alpha = 0.7f
            holder.deleteBtn.visibility = View.VISIBLE
        }

        // Trophy / action icon
        when {
            item.hasTrophy -> holder.icon.setImageResource(R.drawable.ic_trophy)
            item.hasActions -> holder.icon.setImageResource(R.drawable.ic_flash)
            else -> holder.icon.setImageResource(R.drawable.ic_notifications)
        }

        // Click → detail
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }

        // Delete
        holder.deleteBtn.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onDeleteClick?.invoke(pos)
            }
        }
    }

    override fun getItemCount() = items.size

    fun removeItem(position: Int) {
        if (position in items.indices) {
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
        }
    }

    fun markAllRead() {
        items.forEachIndexed { index, notification ->
            if (!notification.isRead) {
                items[index] = notification.copy(isRead = true)
            }
        }
        notifyDataSetChanged()
    }
}
