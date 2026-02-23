package com.example.alea.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.alea.R
import com.example.alea.data.MockDataProvider

class CreateStep3Fragment : Fragment() {

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

        // Step
        val stepLabel = TextView(requireContext()).apply {
            text = "Paso 4 de 5"
            setTextColor(resources.getColor(R.color.color_text_secondary, null))
            textSize = 13f
            typeface = resources.getFont(R.font.poppins)
            setPadding(0, 24, 0, 8)
        }
        root.addView(stepLabel)

        // Title
        val title = TextView(requireContext()).apply {
            text = getString(R.string.create_step3_title)
            setTextColor(resources.getColor(R.color.white, null))
            textSize = 26f
            typeface = resources.getFont(R.font.poppins_bold)
            setPadding(0, 0, 0, 48)
        }
        root.addView(title)

        // Balance
        val balance = TextView(requireContext()).apply {
            text = getString(R.string.create_bet_your_balance, MockDataProvider.currentUser.aleaCoins)
            setTextColor(resources.getColor(R.color.color_coins, null))
            textSize = 16f
            typeface = resources.getFont(R.font.poppins_semibold)
            setPadding(0, 0, 0, 40)
        }
        root.addView(balance)

        // Amount display
        val amountDisplay = TextView(requireContext()).apply {
            text = "100 ★"
            setTextColor(resources.getColor(R.color.white, null))
            textSize = 48f
            typeface = resources.getFont(R.font.poppins_bold)
            gravity = android.view.Gravity.CENTER
            setPadding(0, 0, 0, 24)
        }
        root.addView(amountDisplay)

        // SeekBar
        val seekBar = SeekBar(requireContext()).apply {
            max = 1000
            progress = 100
            progressTintList = android.content.res.ColorStateList.valueOf(resources.getColor(R.color.color_primary, null))
            thumbTintList = android.content.res.ColorStateList.valueOf(resources.getColor(R.color.color_primary, null))
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    val amount = (progress / 50) * 50
                    amountDisplay.text = "$amount ★"
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
        val seekLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        seekLp.bottomMargin = 16
        root.addView(seekBar, seekLp)

        // Quick picks
        val quickRow = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = android.view.Gravity.CENTER
            setPadding(0, 16, 0, 60)
        }
        listOf(50, 100, 250, 500).forEach { amount ->
            val chip = TextView(requireContext()).apply {
                text = "$amount"
                setTextColor(resources.getColor(R.color.white, null))
                textSize = 14f
                typeface = resources.getFont(R.font.poppins_semibold)
                setBackgroundResource(R.drawable.bg_chip)
                setPadding(32, 16, 32, 16)
                setOnClickListener {
                    seekBar.progress = amount
                    amountDisplay.text = "$amount ★"
                }
            }
            val chipLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            chipLp.marginEnd = 16
            quickRow.addView(chip, chipLp)
        }
        root.addView(quickRow)

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
                findNavController().navigate(R.id.action_step3_to_step4)
            }
        }
        root.addView(nextBtn, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))

        return root
    }
}
