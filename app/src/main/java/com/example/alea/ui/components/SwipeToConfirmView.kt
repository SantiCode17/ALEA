package com.example.alea.ui.components

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import com.example.alea.R

/**
 * Swipe to confirm button with gradient reveal effect
 * Used in the final step of challenge creation
 */
class SwipeToConfirmView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.alea_glass)
    }

    private val gradientPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val thumbPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.alea_text_primary)
        setShadowLayer(8f, 0f, 4f, 0x40000000)
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.alea_text_secondary)
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }

    private val arrowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.alea_background)
        textSize = 48f
        textAlign = Paint.Align.CENTER
    }

    private val primaryStart = ContextCompat.getColor(context, R.color.alea_primary_start)
    private val primaryEnd = ContextCompat.getColor(context, R.color.alea_primary_end)

    private var thumbX = 0f
    private var thumbRadius = 0f
    private val padding = 8f
    private var maxThumbX = 0f
    private var progress = 0f

    private var startText = "Swipe to Create"
    private var confirmText = "Release to Confirm!"

    var onConfirmed: (() -> Unit)? = null

    fun setTexts(start: String, confirm: String) {
        startText = start
        confirmText = confirm
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        thumbRadius = (h - padding * 2) / 2
        thumbX = padding + thumbRadius
        maxThumbX = w - padding - thumbRadius

        // Create gradient
        gradientPaint.shader = LinearGradient(
            0f, 0f, w.toFloat(), 0f,
            primaryStart, primaryEnd,
            Shader.TileMode.CLAMP
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cornerRadius = height / 2f
        val bounds = RectF(0f, 0f, width.toFloat(), height.toFloat())

        // Draw background
        canvas.drawRoundRect(bounds, cornerRadius, cornerRadius, backgroundPaint)

        // Draw gradient progress
        val gradientBounds = RectF(0f, 0f, thumbX + thumbRadius, height.toFloat())
        canvas.save()
        canvas.clipRect(gradientBounds)
        canvas.drawRoundRect(bounds, cornerRadius, cornerRadius, gradientPaint)
        canvas.restore()

        // Draw text
        val text = if (progress >= 0.9f) confirmText else startText
        textPaint.color = if (progress >= 0.9f) {
            ContextCompat.getColor(context, R.color.alea_text_primary)
        } else {
            ContextCompat.getColor(context, R.color.alea_text_secondary)
        }
        val textX = width / 2f + thumbRadius
        val textY = height / 2f + textPaint.textSize / 3
        canvas.drawText(text, textX, textY, textPaint)

        // Draw thumb
        canvas.drawCircle(thumbX, height / 2f, thumbRadius - 4f, thumbPaint)

        // Draw arrow on thumb
        val arrowY = height / 2f + arrowPaint.textSize / 3
        canvas.drawText("→", thumbX, arrowY, arrowPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Only start drag if touching the thumb
                val thumbBounds = RectF(
                    thumbX - thumbRadius,
                    0f,
                    thumbX + thumbRadius,
                    height.toFloat()
                )
                if (thumbBounds.contains(event.x, event.y)) {
                    return true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                thumbX = event.x.coerceIn(padding + thumbRadius, maxThumbX)
                progress = (thumbX - padding - thumbRadius) / (maxThumbX - padding - thumbRadius)
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (progress >= 0.9f) {
                    // Confirmed
                    animateToEnd()
                    onConfirmed?.invoke()
                } else {
                    // Reset
                    animateToStart()
                }
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    private fun animateToStart() {
        ValueAnimator.ofFloat(thumbX, padding + thumbRadius).apply {
            duration = 300
            interpolator = DecelerateInterpolator()
            addUpdateListener { animator ->
                thumbX = animator.animatedValue as Float
                progress = (thumbX - padding - thumbRadius) / (maxThumbX - padding - thumbRadius)
                invalidate()
            }
            start()
        }
    }

    private fun animateToEnd() {
        ValueAnimator.ofFloat(thumbX, maxThumbX).apply {
            duration = 200
            interpolator = DecelerateInterpolator()
            addUpdateListener { animator ->
                thumbX = animator.animatedValue as Float
                progress = 1f
                invalidate()
            }
            start()
        }
    }

    fun reset() {
        thumbX = padding + thumbRadius
        progress = 0f
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredHeight = 160
        val height = resolveSize(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height)
    }
}
