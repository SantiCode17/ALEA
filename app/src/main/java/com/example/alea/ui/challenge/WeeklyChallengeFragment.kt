package com.example.alea.ui.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.alea.R

class WeeklyChallengeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(resources.getColor(R.color.color_background, null))
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
            text = getString(R.string.weekly_title)
            setTextColor(resources.getColor(R.color.white, null))
            textSize = 24f
            typeface = resources.getFont(R.font.poppins_bold)
        }
        root.addView(title)

        val desc = TextView(requireContext()).apply {
            text = getString(R.string.weekly_banner_desc)
            setTextColor(resources.getColor(R.color.color_text_secondary, null))
            textSize = 16f
            typeface = resources.getFont(R.font.poppins)
            setPadding(0, 24, 0, 0)
        }
        root.addView(desc)

        val progress = TextView(requireContext()).apply {
            text = "${getString(R.string.weekly_progress)}\n\n${getString(R.string.weekly_countdown)}"
            setTextColor(resources.getColor(R.color.color_coins, null))
            textSize = 18f
            typeface = resources.getFont(R.font.poppins_semibold)
            setPadding(0, 48, 0, 0)
        }
        root.addView(progress)

        return root
    }
}
