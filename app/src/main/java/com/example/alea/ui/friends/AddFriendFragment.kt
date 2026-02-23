package com.example.alea.ui.friends

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
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

class AddFriendFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val scroll = ScrollView(requireContext()).apply {
            setBackgroundColor(resources.getColor(R.color.color_background, null))
        }

        val root = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 48, 48, 300)
        }

        // Back
        val back = ImageView(requireContext()).apply {
            setImageResource(R.drawable.ic_back)
            setColorFilter(resources.getColor(R.color.white, null))
            setPadding(16, 16, 16, 16)
            setOnClickListener { findNavController().popBackStack() }
        }
        root.addView(back, LinearLayout.LayoutParams(100, 100))

        // Title
        val title = TextView(requireContext()).apply {
            text = getString(R.string.add_friend_title)
            setTextColor(resources.getColor(R.color.white, null))
            textSize = 24f
            typeface = resources.getFont(R.font.poppins_bold)
            setPadding(0, 16, 0, 32)
        }
        root.addView(title)

        // Search bar
        val searchCard = CardView(requireContext()).apply {
            radius = 40f
            setCardBackgroundColor(resources.getColor(R.color.color_surface, null))
            cardElevation = 0f
        }
        val searchInput = EditText(requireContext()).apply {
            hint = getString(R.string.friends_search_hint)
            setHintTextColor(resources.getColor(R.color.color_text_secondary, null))
            setTextColor(resources.getColor(R.color.white, null))
            background = null
            setPadding(48, 32, 48, 32)
            textSize = 15f
            typeface = resources.getFont(R.font.poppins)
            imeOptions = EditorInfo.IME_ACTION_SEARCH
            isSingleLine = true
        }
        searchCard.addView(searchInput)
        val searchLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        searchLp.bottomMargin = 48
        root.addView(searchCard, searchLp)

        // Suggestions label
        val sugLabel = TextView(requireContext()).apply {
            text = getString(R.string.add_friend_suggestions)
            setTextColor(resources.getColor(R.color.color_text_secondary, null))
            textSize = 14f
            typeface = resources.getFont(R.font.poppins_semibold)
            setPadding(0, 0, 0, 24)
        }
        root.addView(sugLabel)

        // Suggestions
        MockDataProvider.contactSuggestions.forEach { friend ->
            val card = CardView(requireContext()).apply {
                radius = 40f
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
            row.addView(avatar, LinearLayout.LayoutParams(120, 120))

            val info = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(24, 0, 0, 0)
            }
            info.addView(TextView(requireContext()).apply {
                text = friend.name
                setTextColor(resources.getColor(R.color.white, null))
                textSize = 16f
                typeface = resources.getFont(R.font.poppins_semibold)
            })
            info.addView(TextView(requireContext()).apply {
                text = friend.username
                setTextColor(resources.getColor(R.color.color_text_secondary, null))
                textSize = 13f
                typeface = resources.getFont(R.font.poppins)
            })
            val infoLp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            row.addView(info, infoLp)

            val addBtn = TextView(requireContext()).apply {
                text = getString(R.string.add_friend_button)
                setTextColor(resources.getColor(R.color.white, null))
                textSize = 13f
                typeface = resources.getFont(R.font.poppins_semibold)
                setBackgroundResource(R.drawable.bg_gradient_primary)
                setPadding(40, 20, 40, 20)
                setOnClickListener {
                    this.text = "✓ Enviado"
                    this.isEnabled = false
                    this.alpha = 0.6f
                    Toast.makeText(requireContext(), "Solicitud enviada a ${friend.name}", Toast.LENGTH_SHORT).show()
                }
            }
            row.addView(addBtn)

            card.addView(row)
            val cardLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            cardLp.bottomMargin = 20
            root.addView(card, cardLp)
        }

        scroll.addView(root)
        return scroll
    }
}
