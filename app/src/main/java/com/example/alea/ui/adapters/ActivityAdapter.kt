package com.example.alea.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alea.R
import com.example.alea.data.model.Notification

class ActivityAdapter(private val items: List<Notification>) :
    RecyclerView.Adapter<ActivityAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.activity_text)
        val time: TextView = view.findViewById(R.id.activity_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        val prefix = if (item.senderName.isNotEmpty()) "${item.senderName} " else ""
        holder.text.text = "$prefix${item.text}"
        holder.time.text = item.time
    }

    override fun getItemCount() = items.size
}
