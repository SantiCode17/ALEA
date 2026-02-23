package com.example.alea.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.alea.R

class SecurityFragment : Fragment() {

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

        // Title
        val title = TextView(requireContext()).apply {
            text = getString(R.string.security_title)
            setTextColor(resources.getColor(R.color.white, null))
            textSize = 24f
            typeface = resources.getFont(R.font.poppins_bold)
            setPadding(0, 16, 0, 40)
        }
        root.addView(title)

        // Security rows
        fun addSecurityRow(label: String, hasSwitch: Boolean = false, onClick: (() -> Unit)? = null) {
            val card = CardView(requireContext()).apply {
                radius = 36f
                setCardBackgroundColor(resources.getColor(R.color.color_surface, null))
                cardElevation = 0f
            }
            val row = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(40, 36, 40, 36)
                gravity = android.view.Gravity.CENTER_VERTICAL
            }
            row.addView(TextView(requireContext()).apply {
                text = label
                setTextColor(resources.getColor(R.color.white, null))
                textSize = 16f
                typeface = resources.getFont(R.font.poppins)
            }, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))

            if (hasSwitch) {
                val switch = Switch(requireContext()).apply {
                    isChecked = false
                    thumbTintList = android.content.res.ColorStateList.valueOf(resources.getColor(R.color.color_primary, null))
                    trackTintList = android.content.res.ColorStateList.valueOf(resources.getColor(R.color.color_surface_light, null))
                }
                row.addView(switch)
            } else {
                val arrow = ImageView(requireContext()).apply {
                    setImageResource(R.drawable.ic_arrow_right)
                    setColorFilter(resources.getColor(R.color.color_text_secondary, null))
                }
                row.addView(arrow, LinearLayout.LayoutParams(56, 56))
            }

            card.addView(row)
            card.setOnClickListener {
                onClick?.invoke()
            }
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lp.bottomMargin = 16
            root.addView(card, lp)
        }

        addSecurityRow(getString(R.string.security_change_pass)) {
            Toast.makeText(requireContext(), "Función no disponible en modo demo", Toast.LENGTH_SHORT).show()
        }
        addSecurityRow(getString(R.string.security_2fa), hasSwitch = true)
        addSecurityRow(getString(R.string.security_sessions)) {
            Toast.makeText(requireContext(), "1 sesión activa (este dispositivo)", Toast.LENGTH_SHORT).show()
        }

        // Spacer
        root.addView(View(requireContext()), LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 60))

        // Delete account
        val deleteBtn = TextView(requireContext()).apply {
            text = getString(R.string.security_delete)
            setTextColor(resources.getColor(R.color.color_error, null))
            textSize = 16f
            typeface = resources.getFont(R.font.poppins_semibold)
            gravity = android.view.Gravity.CENTER
            setPadding(0, 24, 0, 24)
            setOnClickListener {
                requireActivity().getSharedPreferences("alea_prefs", 0).edit()
                    .putBoolean("is_logged_in", false)
                    .putBoolean("is_first_launch", true)
                    .apply()
                Toast.makeText(requireContext(), "Cuenta eliminada", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), com.example.alea.ui.onboarding.OnboardingActivity::class.java))
                requireActivity().finish()
            }
        }
        root.addView(deleteBtn)

        scroll.addView(root)
        return scroll
    }
}
