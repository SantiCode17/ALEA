package com.example.alea.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alea.R
import com.example.alea.data.model.Achievement

class AchievementAdapter(
    private val items: List<Achievement>,
    private val horizontalMode: Boolean = false
) : RecyclerView.Adapter<AchievementAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val icon: TextView = view.findViewById(R.id.achievement_icon)
        val name: TextView = view.findViewById(R.id.achievement_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_achievement, parent, false)
        if (horizontalMode) {
            val dp100 = (100 * parent.context.resources.displayMetrics.density).toInt()
            val dp12 = (12 * parent.context.resources.displayMetrics.density).toInt()
            view.layoutParams = ViewGroup.MarginLayoutParams(dp100, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                marginEnd = dp12
            }
        }
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.icon.text = item.emoji
        holder.name.text = item.name
        holder.itemView.alpha = if (item.isUnlocked) 1f else 0.4f
    }

    override fun getItemCount() = items.size
}
