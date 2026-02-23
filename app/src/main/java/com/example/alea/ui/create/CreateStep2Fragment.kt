package com.example.alea.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.alea.R
import com.example.alea.data.MockDataProvider
import de.hdodenhof.circleimageview.CircleImageView

class CreateStep2Fragment : Fragment() {

    private val selectedFriends = mutableSetOf<Int>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val scroll = ScrollView(requireContext()).apply {
            setBackgroundColor(resources.getColor(R.color.color_background, null))
        }

        val root = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
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

        // Step
        val stepLabel = TextView(requireContext()).apply {
            text = "Paso 3 de 5"
            setTextColor(resources.getColor(R.color.color_text_secondary, null))
            textSize = 13f
            typeface = resources.getFont(R.font.poppins)
            setPadding(0, 24, 0, 8)
        }
        root.addView(stepLabel)

        // Title
        val title = TextView(requireContext()).apply {
            text = getString(R.string.create_step2_title)
            setTextColor(resources.getColor(R.color.white, null))
            textSize = 26f
            typeface = resources.getFont(R.font.poppins_bold)
            setPadding(0, 0, 0, 16)
        }
        root.addView(title)

        val hint = TextView(requireContext()).apply {
            text = getString(R.string.create_select_rivals_hint)
            setTextColor(resources.getColor(R.color.color_text_secondary, null))
            textSize = 14f
            typeface = resources.getFont(R.font.poppins)
            setPadding(0, 0, 0, 32)
        }
        root.addView(hint)

        // Friends list
        MockDataProvider.friends.forEachIndexed { index, friend ->
            val card = CardView(requireContext()).apply {
                radius = 36f
                setCardBackgroundColor(resources.getColor(R.color.color_surface, null))
                cardElevation = 0f
            }
            val row = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(36, 28, 36, 28)
                gravity = android.view.Gravity.CENTER_VERTICAL
            }

            val avatar = CircleImageView(requireContext()).apply {
                setImageResource(R.drawable.ic_person)
                setColorFilter(resources.getColor(R.color.color_primary, null))
                borderWidth = 2
                borderColor = resources.getColor(R.color.color_primary, null)
            }
            row.addView(avatar, LinearLayout.LayoutParams(100, 100))

            val info = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(24, 0, 0, 0)
            }
            info.addView(TextView(requireContext()).apply {
                text = friend.name
                setTextColor(resources.getColor(R.color.white, null))
                textSize = 15f
                typeface = resources.getFont(R.font.poppins_semibold)
            })
            info.addView(TextView(requireContext()).apply {
                text = friend.username
                setTextColor(resources.getColor(R.color.color_text_secondary, null))
                textSize = 12f
                typeface = resources.getFont(R.font.poppins)
            })
            row.addView(info, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))

            val check = TextView(requireContext()).apply {
                text = "○"
                textSize = 24f
                setTextColor(resources.getColor(R.color.color_text_secondary, null))
            }
            row.addView(check)

            card.setOnClickListener {
                if (selectedFriends.contains(index)) {
                    selectedFriends.remove(index)
                    check.text = "○"
                    check.setTextColor(resources.getColor(R.color.color_text_secondary, null))
                    card.setCardBackgroundColor(resources.getColor(R.color.color_surface, null))
                } else {
                    selectedFriends.add(index)
                    check.text = "✓"
                    check.setTextColor(resources.getColor(R.color.color_primary, null))
                    card.setCardBackgroundColor(resources.getColor(R.color.color_surface_light, null))
                }
            }

            card.addView(row)
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lp.bottomMargin = 16
            root.addView(card, lp)
        }

        // Spacer
        root.addView(View(requireContext()), LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 40))

        // Next
        val nextBtn = TextView(requireContext()).apply {
            text = getString(R.string.create_next)
            setTextColor(resources.getColor(R.color.white, null))
            textSize = 17f
            typeface = resources.getFont(R.font.poppins_bold)
            setBackgroundResource(R.drawable.bg_gradient_primary)
            setPadding(60, 40, 60, 40)
            gravity = android.view.Gravity.CENTER
            setOnClickListener {
                if (selectedFriends.isEmpty()) {
                    Toast.makeText(requireContext(), getString(R.string.create_select_rivals_hint), Toast.LENGTH_SHORT).show()
                } else {
                    findNavController().navigate(R.id.action_step2_to_step3)
                }
            }
        }
        root.addView(nextBtn, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))

        scroll.addView(root)
        return scroll
    }
}
