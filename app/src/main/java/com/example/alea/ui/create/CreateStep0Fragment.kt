package com.example.alea.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.alea.R

class CreateStep0Fragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(resources.getColor(R.color.color_background, null))
            setPadding(48, 48, 48, 48)
        }

        // Back
        val back = ImageView(requireContext()).apply {
            setImageResource(R.drawable.ic_back)
            setColorFilter(resources.getColor(R.color.white, null))
            setPadding(16, 16, 16, 16)
            setOnClickListener { findNavController().popBackStack() }
        }
        root.addView(back, LinearLayout.LayoutParams(100, 100))

        // Step indicator
        val stepLabel = TextView(requireContext()).apply {
            text = "Paso 1 de 5"
            setTextColor(resources.getColor(R.color.color_text_secondary, null))
            textSize = 13f
            typeface = resources.getFont(R.font.poppins)
            setPadding(0, 24, 0, 8)
        }
        root.addView(stepLabel)

        // Title
        val title = TextView(requireContext()).apply {
            text = getString(R.string.create_step0_title)
            setTextColor(resources.getColor(R.color.white, null))
            textSize = 26f
            typeface = resources.getFont(R.font.poppins_bold)
            setPadding(0, 0, 0, 48)
        }
        root.addView(title)

        // Type cards
        fun addTypeCard(emoji: String, label: String, desc: String) {
            val card = CardView(requireContext()).apply {
                radius = 40f
                setCardBackgroundColor(resources.getColor(R.color.color_surface, null))
                cardElevation = 0f
            }
            val inner = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(40, 36, 40, 36)
                gravity = android.view.Gravity.CENTER_VERTICAL
            }
            val emojiTv = TextView(requireContext()).apply {
                text = emoji
                textSize = 36f
            }
            inner.addView(emojiTv, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT))

            val texts = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(32, 0, 0, 0)
            }
            texts.addView(TextView(requireContext()).apply {
                text = label
                setTextColor(resources.getColor(R.color.white, null))
                textSize = 18f
                typeface = resources.getFont(R.font.poppins_semibold)
            })
            texts.addView(TextView(requireContext()).apply {
                text = desc
                setTextColor(resources.getColor(R.color.color_text_secondary, null))
                textSize = 13f
                typeface = resources.getFont(R.font.poppins)
            })
            inner.addView(texts, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))

            card.addView(inner)
            card.setOnClickListener {
                findNavController().navigate(R.id.action_step0_to_step1)
            }
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lp.bottomMargin = 24
            root.addView(card, lp)
        }

        addTypeCard("🎯", getString(R.string.create_type_dare), "Desafía a tus amigos a hacer algo loco")
        addTypeCard("💰", getString(R.string.create_type_bet), "Apuesta Alea Coins por un resultado")
        addTypeCard("🗳️", getString(R.string.create_type_vote), "Crea una votación grupal")

        return root
    }
}
