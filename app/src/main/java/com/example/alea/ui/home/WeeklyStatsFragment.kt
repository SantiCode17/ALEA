package com.example.alea.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.alea.R

class WeeklyStatsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val scroll = ScrollView(requireContext()).apply {
            setBackgroundColor(resources.getColor(R.color.color_background, null))
        }

        val root = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 48, 48, 48)
        }

        val back = ImageView(requireContext()).apply {
            setImageResource(R.drawable.ic_back)
            setColorFilter(resources.getColor(R.color.white, null))
            setPadding(16, 16, 16, 16)
            setOnClickListener { findNavController().popBackStack() }
        }
        root.addView(back, LinearLayout.LayoutParams(100, 100))

        val title = TextView(requireContext()).apply {
            text = getString(R.string.weekly_stats)
            setTextColor(resources.getColor(R.color.white, null))
            textSize = 24f
            typeface = resources.getFont(R.font.poppins_bold)
            setPadding(0, 16, 0, 32)
        }
        root.addView(title)

        fun addStatCard(label: String, value: String, color: Int) {
            val card = CardView(requireContext()).apply {
                radius = 40f
                setCardBackgroundColor(resources.getColor(R.color.color_surface, null))
                cardElevation = 0f
            }
            val inner = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(48, 36, 48, 36)
            }
            inner.addView(TextView(requireContext()).apply {
                text = value
                textSize = 32f
                setTextColor(color)
                typeface = resources.getFont(R.font.poppins_bold)
            })
            inner.addView(TextView(requireContext()).apply {
                text = label
                textSize = 14f
                setTextColor(resources.getColor(R.color.color_text_secondary, null))
                typeface = resources.getFont(R.font.poppins)
            })
            card.addView(inner)
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lp.bottomMargin = 24
            root.addView(card, lp)
        }

        addStatCard("Retos completados", "12", resources.getColor(R.color.color_primary, null))
        addStatCard("Victorias", "8", resources.getColor(R.color.color_coins, null))
        addStatCard("Derrotas", "4", Color.parseColor("#FF5252"))
        addStatCard("Monedas ganadas", "+2,450", resources.getColor(R.color.color_coins, null))
        addStatCard("Monedas apostadas", "3,100", resources.getColor(R.color.color_text_secondary, null))
        addStatCard("Racha actual", "5 días 🔥", resources.getColor(R.color.color_primary, null))

        scroll.addView(root)
        return scroll
    }
}
