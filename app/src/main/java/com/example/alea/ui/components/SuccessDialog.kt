package com.example.alea.ui.components

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.animation.OvershootInterpolator
import com.example.alea.databinding.DialogSuccessBinding

/**
 * Success dialog with celebration animation
 * Used when a challenge is created successfully
 */
class SuccessDialog(
    context: Context,
    private val onViewChallenge: (() -> Unit)? = null,
    private val onDone: (() -> Unit)? = null
) : Dialog(context) {

    private lateinit var binding: DialogSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = DialogSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(true)

        setupClickListeners()
        animateEntrance()
    }

    private fun setupClickListeners() {
        binding.viewChallengeButton.setOnClickListener {
            onViewChallenge?.invoke()
            dismiss()
        }

        binding.doneButton.setOnClickListener {
            onDone?.invoke()
            dismiss()
        }
    }

    private fun animateEntrance() {
        val container = binding.root
        container.scaleX = 0.8f
        container.scaleY = 0.8f
        container.alpha = 0f

        container.animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setDuration(400)
            .setInterpolator(OvershootInterpolator(1.5f))
            .start()

        // Pulse the emoji
        binding.successEmoji.animate()
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(300)
            .setStartDelay(300)
            .withEndAction {
                binding.successEmoji.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(200)
                    .start()
            }
            .start()
    }

    companion object {
        fun show(
            context: Context,
            onViewChallenge: (() -> Unit)? = null,
            onDone: (() -> Unit)? = null
        ): SuccessDialog {
            return SuccessDialog(context, onViewChallenge, onDone).also { it.show() }
        }
    }
}
