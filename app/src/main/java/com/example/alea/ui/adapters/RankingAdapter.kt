package com.example.alea.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.alea.R
import com.example.alea.data.model.RankUser

class RankingAdapter(
    private val items: List<RankUser>,
    private val onClick: (RankUser) -> Unit
) : RecyclerView.Adapter<RankingAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val position: TextView = view.findViewById(R.id.ranking_position)
        val name: TextView = view.findViewById(R.id.ranking_name)
        val wins: TextView = view.findViewById(R.id.ranking_wins)
        val coins: TextView = view.findViewById(R.id.ranking_coins)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        LayoutInflater.from(parent.context).inflate(R.layout.item_ranking, parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.position.text = item.position
        holder.name.text = item.name
        holder.wins.text = item.username
        holder.coins.text = "${item.points} pts"

        if (item.isCurrentUser) {
            holder.name.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.color_primary_start))
        } else {
            holder.name.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        }

        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = items.size
}
