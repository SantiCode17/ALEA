package com.example.alea.ui.friends

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
import com.example.alea.data.MockDataProvider
import de.hdodenhof.circleimageview.CircleImageView

class FriendProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val scroll = ScrollView(requireContext()).apply {
            setBackgroundColor(resources.getColor(R.color.color_background, null))
        }

        val root = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
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

        val friend = MockDataProvider.friends.firstOrNull() ?: return root

        // Avatar
        val avatar = CircleImageView(requireContext()).apply {
            setImageResource(R.drawable.ic_person)
            setColorFilter(resources.getColor(R.color.color_primary, null))
            borderWidth = 4
            borderColor = resources.getColor(R.color.color_primary, null)
        }
        val avatarLp = LinearLayout.LayoutParams(280, 280)
        avatarLp.topMargin = 32
        root.addView(avatar, avatarLp)

        // Name
        val name = TextView(requireContext()).apply {
            text = friend.name
            setTextColor(resources.getColor(R.color.white, null))
            textSize = 24f
            typeface = resources.getFont(R.font.poppins_bold)
            gravity = android.view.Gravity.CENTER
            setPadding(0, 24, 0, 0)
        }
        root.addView(name)

        // Username
        val username = TextView(requireContext()).apply {
            text = friend.username
            setTextColor(resources.getColor(R.color.color_text_secondary, null))
            textSize = 15f
            typeface = resources.getFont(R.font.poppins)
            gravity = android.view.Gravity.CENTER
        }
        root.addView(username)

        // Stats row
        val statsRow = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = android.view.Gravity.CENTER
            setPadding(0, 40, 0, 40)
        }

        fun addStat(value: String, label: String) {
            val col = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                gravity = android.view.Gravity.CENTER
                setPadding(40, 0, 40, 0)
            }
            col.addView(TextView(requireContext()).apply {
                text = value
                setTextColor(resources.getColor(R.color.white, null))
                textSize = 22f
                typeface = resources.getFont(R.font.poppins_bold)
                gravity = android.view.Gravity.CENTER
            })
            col.addView(TextView(requireContext()).apply {
                text = label
                setTextColor(resources.getColor(R.color.color_text_secondary, null))
                textSize = 12f
                typeface = resources.getFont(R.font.poppins)
                gravity = android.view.Gravity.CENTER
            })
            statsRow.addView(col)
        }

        addStat("7", "Victorias")
        addStat("3", "Racha")
        addStat("1,280", "Puntos")

        root.addView(statsRow)

        // Level
        val levelCard = CardView(requireContext()).apply {
            radius = 40f
            setCardBackgroundColor(resources.getColor(R.color.color_surface, null))
            cardElevation = 0f
        }
        val levelInner = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 36, 48, 36)
            gravity = android.view.Gravity.CENTER
        }
        levelInner.addView(TextView(requireContext()).apply {
            text = "Nivel 5"
            setTextColor(resources.getColor(R.color.color_coins, null))
            textSize = 20f
            typeface = resources.getFont(R.font.poppins_bold)
            gravity = android.view.Gravity.CENTER
        })
        levelInner.addView(TextView(requireContext()).apply {
            text = "🏆 Competidor"
            setTextColor(resources.getColor(R.color.color_text_secondary, null))
            textSize = 14f
            typeface = resources.getFont(R.font.poppins)
            gravity = android.view.Gravity.CENTER
        })
        levelCard.addView(levelInner)
        val levelLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        levelLp.bottomMargin = 32
        root.addView(levelCard, levelLp)

        // Action buttons
        val chatBtn = TextView(requireContext()).apply {
            text = "💬  Enviar mensaje"
            setTextColor(resources.getColor(R.color.white, null))
            textSize = 16f
            typeface = resources.getFont(R.font.poppins_semibold)
            setBackgroundResource(R.drawable.bg_gradient_primary)
            setPadding(60, 36, 60, 36)
            gravity = android.view.Gravity.CENTER
            setOnClickListener {
                findNavController().navigate(R.id.action_friendProfile_to_chat)
            }
        }
        val chatLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        chatLp.bottomMargin = 20
        root.addView(chatBtn, chatLp)

        val challengeBtn = TextView(requireContext()).apply {
            text = "⚔️  Retar"
            setTextColor(resources.getColor(R.color.white, null))
            textSize = 16f
            typeface = resources.getFont(R.font.poppins_semibold)
            setBackgroundResource(R.drawable.bg_gradient_violet)
            setPadding(60, 36, 60, 36)
            gravity = android.view.Gravity.CENTER
            setOnClickListener {
                findNavController().navigate(R.id.action_friendProfile_to_createStep0)
            }
        }
        root.addView(challengeBtn, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))

        scroll.addView(root)
        return scroll
    }
}
