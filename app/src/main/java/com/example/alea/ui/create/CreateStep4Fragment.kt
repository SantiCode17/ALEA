package com.example.alea.ui.create

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.alea.R

class CreateStep4Fragment : Fragment() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val ctx = requireContext()

        val scrollView = android.widget.ScrollView(ctx).apply {
            setBackgroundColor(resources.getColor(R.color.color_background, null))
            clipToPadding = false
        }

        val root = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(52, 40, 52, 120)
            gravity = Gravity.CENTER_HORIZONTAL
        }

        // Back
        val backRow = LinearLayout(ctx).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.START
        }
        val back = ImageView(ctx).apply {
            setImageResource(R.drawable.ic_back)
            setColorFilter(resources.getColor(R.color.white, null))
            setPadding(16, 16, 16, 16)
            setOnClickListener { findNavController().popBackStack() }
        }
        backRow.addView(back, LinearLayout.LayoutParams(100, 100))
        root.addView(backRow, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))

        // Step indicator
        val stepLabel = TextView(ctx).apply {
            text = "Paso 5 de 5"
            setTextColor(resources.getColor(R.color.color_text_secondary, null))
            textSize = 13f
            typeface = resources.getFont(R.font.poppins)
            setPadding(0, 16, 0, 8)
            gravity = Gravity.CENTER
        }
        root.addView(stepLabel)

        // Title
        val title = TextView(ctx).apply {
            text = getString(R.string.create_step4_title)
            setTextColor(resources.getColor(R.color.white, null))
            textSize = 24f
            typeface = resources.getFont(R.font.poppins_bold)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 32)
        }
        root.addView(title)

        // Summary card
        val summaryCard = CardView(ctx).apply {
            radius = 48f
            setCardBackgroundColor(resources.getColor(R.color.color_surface, null))
            cardElevation = 0f
        }
        val summaryInner = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 36, 48, 36)
        }

        fun addRow(label: String, value: String) {
            val row = LinearLayout(ctx).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(0, 10, 0, 10)
            }
            row.addView(TextView(ctx).apply {
                text = label
                setTextColor(resources.getColor(R.color.color_text_secondary, null))
                textSize = 14f
                typeface = resources.getFont(R.font.poppins)
            }, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
            row.addView(TextView(ctx).apply {
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
        summaryLp.bottomMargin = 48
        root.addView(summaryCard, summaryLp)

        // Emoji
        val emoji = TextView(ctx).apply {
            text = "⚡"
            textSize = 52f
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 32)
        }
        root.addView(emoji)

        // ─── Professional Swipe-to-Confirm ───

        // Track container (rounded pill)
        val density = resources.displayMetrics.density
        val trackHeight = (64 * density).toInt()
        val thumbSize = (52 * density).toInt()
        val trackPadding = (6 * density).toInt()

        val trackBg = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = trackHeight.toFloat() / 2f
            setColor(resources.getColor(R.color.color_surface, null))
        }

        val trackContainer = FrameLayout(ctx).apply {
            background = trackBg
            clipChildren = false
            clipToPadding = false
        }

        // Fill gradient (grows as thumb slides)
        val fillGradient = View(ctx).apply {
            val gd = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(
                    Color.parseColor("#33FF8C42"),
                    Color.parseColor("#33FF4B6A")
                )
            )
            gd.cornerRadius = trackHeight / 2f
            background = gd
            alpha = 0f
        }
        trackContainer.addView(fillGradient, FrameLayout.LayoutParams(0, FrameLayout.LayoutParams.MATCH_PARENT))

        // Swipe hint text
        val swipeHint = TextView(ctx).apply {
            text = getString(R.string.create_confirm_swipe)
            setTextColor(resources.getColor(R.color.color_text_hint, null))
            textSize = 14f
            typeface = resources.getFont(R.font.poppins_medium)
            gravity = Gravity.CENTER
        }
        trackContainer.addView(swipeHint, FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        ).apply { gravity = Gravity.CENTER })

        // Arrows hint
        val arrowsHint = TextView(ctx).apply {
            text = "  ›  ›  ›"
            setTextColor(Color.parseColor("#44FFFFFF"))
            textSize = 16f
            typeface = resources.getFont(R.font.poppins_bold)
            gravity = Gravity.CENTER_VERTICAL or Gravity.START
            setPadding(thumbSize + (20 * density).toInt(), 0, 0, 0)
        }
        trackContainer.addView(arrowsHint, FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        ))

        // Thumb (circular gradient button)
        val thumbBg = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(
                Color.parseColor("#FF8C42"),
                Color.parseColor("#FF4B6A")
            )
        ).apply {
            shape = GradientDrawable.OVAL
        }

        val thumb = FrameLayout(ctx).apply {
            background = thumbBg
            elevation = 12f
        }

        val thumbArrow = TextView(ctx).apply {
            text = "→"
            setTextColor(Color.WHITE)
            textSize = 20f
            typeface = resources.getFont(R.font.poppins_bold)
            gravity = Gravity.CENTER
        }
        thumb.addView(thumbArrow, FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        ).apply { gravity = Gravity.CENTER })

        val thumbLp = FrameLayout.LayoutParams(thumbSize, thumbSize).apply {
            gravity = Gravity.START or Gravity.CENTER_VERTICAL
            leftMargin = trackPadding
            topMargin = trackPadding
        }
        trackContainer.addView(thumb, thumbLp)

        // Success overlay (checkmark)
        val successOverlay = TextView(ctx).apply {
            text = "✓"
            setTextColor(Color.WHITE)
            textSize = 28f
            typeface = resources.getFont(R.font.poppins_bold)
            gravity = Gravity.CENTER
            visibility = View.GONE
        }
        trackContainer.addView(successOverlay, FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        ).apply { gravity = Gravity.CENTER })

        val trackLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, trackHeight + trackPadding * 2)
        trackLp.topMargin = (8 * density).toInt()
        root.addView(trackContainer, trackLp)

        // Pulse animation on thumb
        val pulseX = ObjectAnimator.ofFloat(thumb, "scaleX", 1f, 1.08f, 1f).apply {
            duration = 1500
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }
        val pulseY = ObjectAnimator.ofFloat(thumb, "scaleY", 1f, 1.08f, 1f).apply {
            duration = 1500
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }
        AnimatorSet().apply {
            playTogether(pulseX, pulseY)
            start()
        }

        // Touch handler for swipe
        var startX = 0f
        var maxSlide = 0f

        thumb.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.rawX
                    maxSlide = (trackContainer.width - thumbSize - trackPadding * 2).toFloat()
                    pulseX.cancel()
                    pulseY.cancel()
                    v.scaleX = 1.1f
                    v.scaleY = 1.1f
                    vibrateLight()
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val delta = (event.rawX - startX).coerceIn(0f, maxSlide)
                    v.translationX = delta
                    val progress = delta / maxSlide

                    // Update fill
                    fillGradient.alpha = progress * 0.8f
                    val fillWidth = (thumbSize + delta + trackPadding).toInt()
                    fillGradient.layoutParams = fillGradient.layoutParams.apply { width = fillWidth }

                    // Fade hint text
                    swipeHint.alpha = 1f - progress * 2f
                    arrowsHint.alpha = 1f - progress * 2f

                    // Change arrow to check near end
                    if (progress > 0.8f) {
                        thumbArrow.text = "✓"
                    } else {
                        thumbArrow.text = "→"
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    val delta = (event.rawX - startX).coerceIn(0f, maxSlide)
                    val progress = delta / maxSlide

                    if (progress > 0.75f) {
                        // SUCCESS
                        vibrateSuccess()
                        v.animate().translationX(maxSlide).setDuration(150).start()
                        fillGradient.animate().alpha(1f).setDuration(200).start()

                        // Show success
                        swipeHint.visibility = View.GONE
                        arrowsHint.visibility = View.GONE
                        thumb.animate().alpha(0f).setDuration(300).start()

                        // Change track to full gradient
                        val successBg = GradientDrawable(
                            GradientDrawable.Orientation.LEFT_RIGHT,
                            intArrayOf(
                                Color.parseColor("#FF8C42"),
                                Color.parseColor("#FF4B6A")
                            )
                        ).apply {
                            shape = GradientDrawable.RECTANGLE
                            cornerRadius = trackHeight / 2f
                        }
                        trackContainer.background = successBg
                        fillGradient.visibility = View.GONE
                        successOverlay.visibility = View.VISIBLE
                        successOverlay.scaleX = 0f
                        successOverlay.scaleY = 0f
                        successOverlay.animate()
                            .scaleX(1f).scaleY(1f)
                            .setDuration(300)
                            .setInterpolator(OvershootInterpolator())
                            .start()

                        Toast.makeText(ctx, getString(R.string.create_confirmed), Toast.LENGTH_LONG).show()
                        v.postDelayed({
                            findNavController().popBackStack(R.id.homeFragment, false)
                        }, 1500)
                    } else {
                        // Reset
                        v.animate().translationX(0f).scaleX(1f).scaleY(1f).setDuration(300)
                            .setInterpolator(OvershootInterpolator()).start()
                        fillGradient.animate().alpha(0f).setDuration(200).start()
                        swipeHint.animate().alpha(1f).setDuration(200).start()
                        arrowsHint.animate().alpha(1f).setDuration(200).start()
                        thumbArrow.text = "→"
                        pulseX.start()
                        pulseY.start()
                    }
                    true
                }
                else -> false
            }
        }

        scrollView.addView(root)
        return scrollView
    }

    private fun vibrateLight() {
        try {
            val vibrator = requireContext().getSystemService(android.content.Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
            }
        } catch (_: Exception) {}
    }

    private fun vibrateSuccess() {
        try {
            val vibrator = requireContext().getSystemService(android.content.Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            }
        } catch (_: Exception) {}
    }
}
