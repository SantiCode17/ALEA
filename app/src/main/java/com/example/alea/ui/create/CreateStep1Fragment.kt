package com.example.alea.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.alea.R

class CreateStep1Fragment : Fragment() {

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
            text = "Paso 2 de 5"
            setTextColor(resources.getColor(R.color.color_text_secondary, null))
            textSize = 13f
            typeface = resources.getFont(R.font.poppins)
            setPadding(0, 24, 0, 8)
        }
        root.addView(stepLabel)

        // Title
        val title = TextView(requireContext()).apply {
            text = getString(R.string.create_step1_title)
            setTextColor(resources.getColor(R.color.white, null))
            textSize = 26f
            typeface = resources.getFont(R.font.poppins_bold)
            setPadding(0, 0, 0, 48)
        }
        root.addView(title)

        // Name field
        val nameLabel = TextView(requireContext()).apply {
            text = getString(R.string.create_challenge_name_label)
            setTextColor(resources.getColor(R.color.color_text_secondary, null))
            textSize = 14f
            typeface = resources.getFont(R.font.poppins_semibold)
            setPadding(0, 0, 0, 12)
        }
        root.addView(nameLabel)

        val nameCard = CardView(requireContext()).apply {
            radius = 30f
            setCardBackgroundColor(resources.getColor(R.color.color_surface, null))
            cardElevation = 0f
        }
        val nameInput = EditText(requireContext()).apply {
            hint = getString(R.string.create_challenge_name_hint)
            setHintTextColor(resources.getColor(R.color.color_text_secondary, null))
            setTextColor(resources.getColor(R.color.white, null))
            background = null
            setPadding(40, 32, 40, 32)
            textSize = 15f
            typeface = resources.getFont(R.font.poppins)
            isSingleLine = true
        }
        nameCard.addView(nameInput)
        val nameLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        nameLp.bottomMargin = 40
        root.addView(nameCard, nameLp)

        // Description field
        val descLabel = TextView(requireContext()).apply {
            text = getString(R.string.create_challenge_desc_label)
            setTextColor(resources.getColor(R.color.color_text_secondary, null))
            textSize = 14f
            typeface = resources.getFont(R.font.poppins_semibold)
            setPadding(0, 0, 0, 12)
        }
        root.addView(descLabel)

        val descCard = CardView(requireContext()).apply {
            radius = 30f
            setCardBackgroundColor(resources.getColor(R.color.color_surface, null))
            cardElevation = 0f
        }
        val descInput = EditText(requireContext()).apply {
            hint = getString(R.string.create_challenge_desc_hint)
            setHintTextColor(resources.getColor(R.color.color_text_secondary, null))
            setTextColor(resources.getColor(R.color.white, null))
            background = null
            setPadding(40, 32, 40, 32)
            textSize = 15f
            typeface = resources.getFont(R.font.poppins)
            minLines = 4
            gravity = android.view.Gravity.TOP
        }
        descCard.addView(descInput)
        val descLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        descLp.bottomMargin = 60
        root.addView(descCard, descLp)

        // Next button
        val nextBtn = TextView(requireContext()).apply {
            text = getString(R.string.create_next)
            setTextColor(resources.getColor(R.color.white, null))
            textSize = 17f
            typeface = resources.getFont(R.font.poppins_bold)
            setBackgroundResource(R.drawable.bg_gradient_primary)
            setPadding(60, 40, 60, 40)
            gravity = android.view.Gravity.CENTER
            setOnClickListener {
                findNavController().navigate(R.id.action_step1_to_step2)
            }
        }
        root.addView(nextBtn, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))

        return root
    }
}
