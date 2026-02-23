package com.example.alea.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alea.R
import com.example.alea.data.model.Message

class MessageAdapter(private val items: MutableList<Message>) :
    RecyclerView.Adapter<MessageAdapter.VH>() {

    companion object {
        private const val TYPE_SENT = 0
        private const val TYPE_RECEIVED = 1
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.message_text)
        val time: TextView = view.findViewById(R.id.message_time)
    }

    override fun getItemViewType(position: Int) =
        if (items[position].isMe) TYPE_SENT else TYPE_RECEIVED

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layout = if (viewType == TYPE_SENT) R.layout.item_message_sent else R.layout.item_message_received
        return VH(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.text.text = item.text
        holder.time.text = item.time
    }

    override fun getItemCount() = items.size

    fun addMessage(message: Message) {
        items.add(message)
        notifyItemInserted(items.size - 1)
    }
}
