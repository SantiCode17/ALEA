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
    private val items: List<Notification>
) : RecyclerView.Adapter<NotificationAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.notification_icon)
        val title: TextView = view.findViewById(R.id.notification_title)
        val message: TextView = view.findViewById(R.id.notification_message)
        val time: TextView = view.findViewById(R.id.notification_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.title.text = if (item.senderName.isNotEmpty()) item.senderName else "ALEA"
        holder.message.text = item.text
        holder.time.text = item.time

        if (!item.isRead) {
            holder.itemView.alpha = 1f
        } else {
            holder.itemView.alpha = 0.7f
        }
    }

    override fun getItemCount() = items.size
}
