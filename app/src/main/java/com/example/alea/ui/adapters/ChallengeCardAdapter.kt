package com.example.alea.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.alea.R
import com.example.alea.data.model.Challenge
import com.example.alea.data.model.ChallengeStatus

class ChallengeCardAdapter(
    private val items: List<Challenge>,
    private val onClick: (Challenge) -> Unit
) : RecyclerView.Adapter<ChallengeCardAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val emoji: TextView = view.findViewById(R.id.challenge_emoji)
        val name: TextView = view.findViewById(R.id.challenge_name)
        val opponent: TextView = view.findViewById(R.id.challenge_opponent)
        val bet: TextView = view.findViewById(R.id.challenge_bet)
        val status: TextView = view.findViewById(R.id.challenge_status)
        val time: TextView = view.findViewById(R.id.challenge_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        LayoutInflater.from(parent.context).inflate(R.layout.item_challenge_card, parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        val ctx = holder.itemView.context

        holder.emoji.text = when (item.category) {
            "Deporte" -> "🏃"
            "Estudio" -> "📚"
            "Humor" -> "😂"
            "Habilidad" -> "🎯"
            "Resistencia" -> "💪"
            else -> "⚡"
        }

        holder.name.text = item.title
        holder.opponent.text = "vs ${item.challenger}"
        holder.bet.text = "${item.bet}"

        when (item.status) {
            ChallengeStatus.ACTIVE -> {
                holder.status.text = "Activo"
                holder.status.setTextColor(ContextCompat.getColor(ctx, R.color.color_success))
            }
            ChallengeStatus.PENDING -> {
                holder.status.text = "Pendiente"
                holder.status.setTextColor(ContextCompat.getColor(ctx, R.color.color_coins))
            }
            ChallengeStatus.COMPLETED -> {
                holder.status.text = "Completado"
                holder.status.setTextColor(ContextCompat.getColor(ctx, R.color.color_primary_start))
            }
            ChallengeStatus.REJECTED -> {
                holder.status.text = "Rechazado"
                holder.status.setTextColor(ContextCompat.getColor(ctx, R.color.color_error))
            }
        }

        holder.time.text = if (item.deadline.isNotEmpty()) item.deadline else "Sin límite"

        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = items.size
}
