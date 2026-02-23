package com.example.alea.ui.ranking

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
import com.example.alea.data.MockDataProvider

class UserRankDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(resources.getColor(R.color.color_background, null))
            setPadding(48, 48, 48, 48)
        }

        // Back button
        val back = ImageView(requireContext()).apply {
            setImageResource(R.drawable.ic_back)
            setColorFilter(resources.getColor(R.color.white, null))
            setPadding(16, 16, 16, 16)
            setOnClickListener { findNavController().popBackStack() }
        }
        root.addView(back, LinearLayout.LayoutParams(100, 100))

        val user = MockDataProvider.weeklyRanking.first()
        val title = TextView(requireContext()).apply {
            text = user.name
            setTextColor(resources.getColor(R.color.white, null))
            textSize = 24f
            typeface = resources.getFont(R.font.poppins_bold)
        }
        root.addView(title)

        val subtitle = TextView(requireContext()).apply {
            text = "${user.username}\n\n${user.points} puntos semanales\n#${user.position} en el ranking"
            setTextColor(resources.getColor(R.color.color_text_secondary, null))
            textSize = 16f
            typeface = resources.getFont(R.font.poppins)
        }
        root.addView(subtitle)

        return root
    }
}
