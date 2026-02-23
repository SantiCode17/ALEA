package com.example.alea.ui.create

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.alea.R

class CreateStep4Fragment : Fragment() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(resources.getColor(R.color.color_background, null))
            setPadding(48, 48, 48, 48)
            gravity = android.view.Gravity.CENTER_HORIZONTAL
        }

        // Back
        val backRow = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = android.view.Gravity.START
        }
        val back = ImageView(requireContext()).apply {
            setImageResource(R.drawable.ic_back)
            setColorFilter(resources.getColor(R.color.white, null))
            setPadding(16, 16, 16, 16)
            setOnClickListener { findNavController().popBackStack() }
        }
        backRow.addView(back, LinearLayout.LayoutParams(100, 100))
        root.addView(backRow, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))

        // Step
        val stepLabel = TextView(requireContext()).apply {
            text = "Paso 5 de 5"
            setTextColor(resources.getColor(R.color.color_text_secondary, null))
            textSize = 13f
            typeface = resources.getFont(R.font.poppins)
            setPadding(0, 24, 0, 8)
            gravity = android.view.Gravity.CENTER
        }
        root.addView(stepLabel)

        // Title
        val title = TextView(requireContext()).apply {
            text = getString(R.string.create_step4_title)
            setTextColor(resources.getColor(R.color.white, null))
            textSize = 26f
            typeface = resources.getFont(R.font.poppins_bold)
            gravity = android.view.Gravity.CENTER
            setPadding(0, 0, 0, 48)
        }
        root.addView(title)

        // Summary card
        val summaryCard = CardView(requireContext()).apply {
            radius = 40f
            setCardBackgroundColor(resources.getColor(R.color.color_surface, null))
            cardElevation = 0f
        }
        val summaryInner = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 40, 48, 40)
        }

        fun addRow(label: String, value: String) {
            val row = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(0, 8, 0, 8)
            }
            row.addView(TextView(requireContext()).apply {
                text = label
                setTextColor(resources.getColor(R.color.color_text_secondary, null))
                textSize = 14f
                typeface = resources.getFont(R.font.poppins)
            }, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
            row.addView(TextView(requireContext()).apply {
                text = value
                setTextColor(resources.getColor(R.color.white, null))
                textSize = 14f
                typeface = resources.getFont(R.font.poppins_semibold)
            })
            summaryInner.addView(row)
        }

        addRow("Tipo", "Reto")
        addRow("Nombre", "Haz 30 flexiones")
        addRow("Rivales", "3 amigos")
        addRow("Apuesta", "100 ★")
        addRow("Duración", "24 horas")

        summaryCard.addView(summaryInner)
        val summaryLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        summaryLp.bottomMargin = 60
        root.addView(summaryCard, summaryLp)

        // Big emoji
        val emoji = TextView(requireContext()).apply {
            text = "⚡"
            textSize = 60f
            gravity = android.view.Gravity.CENTER
            setPadding(0, 0, 0, 24)
        }
        root.addView(emoji)

        // Swipe to create container
        val swipeCard = CardView(requireContext()).apply {
            radius = 60f
            setCardBackgroundColor(resources.getColor(R.color.color_surface, null))
            cardElevation = 0f
        }
        val swipeText = TextView(requireContext()).apply {
            text = getString(R.string.create_confirm_swipe)
            setTextColor(resources.getColor(R.color.color_text_secondary, null))
            textSize = 16f
            typeface = resources.getFont(R.font.poppins_semibold)
            gravity = android.view.Gravity.CENTER
            setPadding(60, 40, 60, 40)
        }

        var startX = 0f
        swipeText.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.x
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val delta = event.x - startX
                    if (delta > 0) {
                        v.translationX = delta.coerceAtMost(200f)
                        v.alpha = 1f - (delta / 400f).coerceAtMost(0.5f)
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    val delta = event.x - startX
                    if (delta > 150f) {
                        // Success
                        Toast.makeText(requireContext(), getString(R.string.create_confirmed), Toast.LENGTH_LONG).show()
                        v.postDelayed({
                            findNavController().popBackStack(R.id.homeFragment, false)
                        }, 1200)
                    } else {
                        v.animate().translationX(0f).alpha(1f).setDuration(200).start()
                    }
                    true
                }
                else -> false
            }
        }

        swipeCard.addView(swipeText)
        root.addView(swipeCard, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))

        return root
    }
}
