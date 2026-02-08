package com.example.alea.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.alea.R
import kotlin.math.max

/**
 * Custom view for displaying weekly performance bar chart
 * with gradient bars and smooth animations
 */
class PerformanceChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.alea_text_secondary)
        textSize = 28f
        textAlign = Paint.Align.CENTER
    }
    private val valuePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.alea_text_primary)
        textSize = 24f
        textAlign = Paint.Align.CENTER
    }

    private val barRect = RectF()
    private val barRadius = 12f

    private var data: List<PerformanceData> = emptyList()
    private var maxValue: Int = 100
    private var animationProgress = 1f

    private val primaryStartColor = ContextCompat.getColor(context, R.color.alea_primary_start)
    private val primaryEndColor = ContextCompat.getColor(context, R.color.alea_primary_end)

    data class PerformanceData(
        val label: String,
        val value: Int,
        val isHighlight: Boolean = false
    )

    fun setData(newData: List<PerformanceData>) {
        data = newData
        maxValue = max(newData.maxOfOrNull { it.value } ?: 1, 1)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (data.isEmpty()) return

        val barCount = data.size
        val totalWidth = width.toFloat()
        val totalHeight = height.toFloat()
        val barSpacing = 16f
        val barWidth = (totalWidth - (barSpacing * (barCount + 1))) / barCount
        val maxBarHeight = totalHeight * 0.65f
        val textBottomMargin = 24f
        val labelTopMargin = 8f

        data.forEachIndexed { index, item ->
            val left = barSpacing + index * (barWidth + barSpacing)
            val barHeight = (item.value.toFloat() / maxValue) * maxBarHeight * animationProgress
            val top = totalHeight - textBottomMargin - labelTopMargin - textPaint.textSize - barHeight
            val bottom = totalHeight - textBottomMargin - labelTopMargin - textPaint.textSize

            // Create gradient for bar
            val gradient = LinearGradient(
                left, top, left, bottom,
                if (item.isHighlight) primaryStartColor else ContextCompat.getColor(context, R.color.alea_surface_variant),
                if (item.isHighlight) primaryEndColor else ContextCompat.getColor(context, R.color.alea_surface),
                Shader.TileMode.CLAMP
            )
            barPaint.shader = gradient

            // Draw bar
            barRect.set(left, top, left + barWidth, bottom)
            canvas.drawRoundRect(barRect, barRadius, barRadius, barPaint)

            // Draw value on top of bar
            if (item.value > 0) {
                canvas.drawText(
                    item.value.toString(),
                    left + barWidth / 2,
                    top - 8f,
                    valuePaint
                )
            }

            // Draw label at bottom
            canvas.drawText(
                item.label,
                left + barWidth / 2,
                totalHeight - 4f,
                textPaint
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredHeight = 200
        val height = resolveSize(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height)
    }

    fun animateChart() {
        animationProgress = 0f
        val animator = android.animation.ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 800
        animator.interpolator = android.view.animation.DecelerateInterpolator()
        animator.addUpdateListener { animation ->
            animationProgress = animation.animatedValue as Float
            invalidate()
        }
        animator.start()
    }
}
