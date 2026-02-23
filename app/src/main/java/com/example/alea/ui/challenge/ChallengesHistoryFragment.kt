package com.example.alea.ui.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alea.R
import com.example.alea.data.MockDataProvider
import com.example.alea.data.model.Challenge
import com.example.alea.data.model.ChallengeStatus

class ChallengesHistoryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(resources.getColor(R.color.color_background, null))
        }

        // Header
        val header = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = android.view.Gravity.CENTER_VERTICAL
            setPadding(48, 48, 48, 24)
        }

        val back = ImageView(requireContext()).apply {
            setImageResource(R.drawable.ic_back)
            setColorFilter(resources.getColor(R.color.white, null))
            setPadding(16, 16, 16, 16)
            setOnClickListener { findNavController().popBackStack() }
        }
        header.addView(back, LinearLayout.LayoutParams(100, 100))

        val title = TextView(requireContext()).apply {
            text = getString(R.string.history_title)
            setTextColor(resources.getColor(R.color.white, null))
            textSize = 24f
            typeface = resources.getFont(R.font.poppins_bold)
            setPadding(16, 0, 0, 0)
        }
        header.addView(title)
        root.addView(header)

        // Stats summary row
        val statsRow = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(48, 0, 48, 32)
        }
        val active = MockDataProvider.challenges.count { it.status == ChallengeStatus.ACTIVE }
        val completed = MockDataProvider.challenges.count { it.status == ChallengeStatus.COMPLETED }
        val pending = MockDataProvider.challenges.count { it.status == ChallengeStatus.PENDING }

        fun addStatChip(label: String, count: Int, color: Int) {
            val chip = TextView(requireContext()).apply {
                text = "$count $label"
                setTextColor(resources.getColor(color, null))
                textSize = 13f
                typeface = resources.getFont(R.font.poppins_semibold)
                setBackgroundResource(R.drawable.shape_chip_inactive)
                setPadding(32, 16, 32, 16)
            }
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lp.marginEnd = 16
            statsRow.addView(chip, lp)
        }
        addStatChip("Activos", active, R.color.color_success)
        addStatChip("Hechos", completed, R.color.color_primary_start)
        addStatChip("Pendientes", pending, R.color.color_coins)
        root.addView(statsRow)

        // RecyclerView with challenge list adapter
        val rv = RecyclerView(requireContext()).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ChallengeListAdapter(MockDataProvider.challenges) {
                findNavController().navigate(R.id.action_history_to_challengeDetail)
            }
            setPadding(48, 0, 48, 200)
            clipToPadding = false
        }
        root.addView(rv, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ))

        return root
    }

    // Inner adapter for list layout
    private class ChallengeListAdapter(
        private val items: List<Challenge>,
        private val onClick: (Challenge) -> Unit
    ) : RecyclerView.Adapter<ChallengeListAdapter.VH>() {

        class VH(view: View) : RecyclerView.ViewHolder(view) {
            val emoji: TextView = view.findViewById(R.id.challenge_emoji)
            val name: TextView = view.findViewById(R.id.challenge_name)
            val opponent: TextView = view.findViewById(R.id.challenge_opponent)
            val bet: TextView = view.findViewById(R.id.challenge_bet)
            val status: TextView = view.findViewById(R.id.challenge_status)
            val time: TextView = view.findViewById(R.id.challenge_time)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_challenge_list, parent, false)
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
            holder.bet.text = "${item.bet} coins"

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
}
