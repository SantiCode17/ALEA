package com.example.alea.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.alea.R

/**
 * Custom color picker view displaying 8 theme colors in a grid
 */
class ColorPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val colors = listOf(
        ContextCompat.getColor(context, R.color.alea_theme_orange),
        ContextCompat.getColor(context, R.color.alea_theme_pink),
        ContextCompat.getColor(context, R.color.alea_theme_violet),
        ContextCompat.getColor(context, R.color.alea_theme_lavender),
        ContextCompat.getColor(context, R.color.alea_theme_green),
        ContextCompat.getColor(context, R.color.alea_theme_yellow),
        ContextCompat.getColor(context, R.color.alea_theme_red),
        ContextCompat.getColor(context, R.color.alea_theme_blue)
    )

    private val colorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 6f
        color = ContextCompat.getColor(context, R.color.alea_text_primary)
    }
    private val checkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        color = ContextCompat.getColor(context, R.color.alea_text_primary)
        strokeCap = Paint.Cap.ROUND
    }

    private var selectedIndex = 0
    private var circleRadius = 0f
    private var circleSpacing = 0f
    private val circleRects = mutableListOf<RectF>()

    var onColorSelected: ((Int) -> Unit)? = null

    fun setSelectedColor(colorInt: Int) {
        val index = colors.indexOf(colorInt)
        if (index >= 0) {
            selectedIndex = index
            invalidate()
        }
    }

    fun getSelectedColor(): Int = colors[selectedIndex]

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateCirclePositions()
    }

    private fun calculateCirclePositions() {
        circleRects.clear()
        val numColors = colors.size
        val totalWidth = width.toFloat()
        val padding = 16f

        circleSpacing = 16f
        circleRadius = ((totalWidth - (padding * 2) - (circleSpacing * (numColors - 1))) / numColors) / 2
        circleRadius = minOf(circleRadius, (height.toFloat() - padding * 2) / 2)

        var currentX = padding + circleRadius
        val centerY = height / 2f

        for (i in colors.indices) {
            circleRects.add(RectF(
                currentX - circleRadius,
                centerY - circleRadius,
                currentX + circleRadius,
                centerY + circleRadius
            ))
            currentX += circleRadius * 2 + circleSpacing
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        circleRects.forEachIndexed { index, rect ->
            val centerX = rect.centerX()
            val centerY = rect.centerY()

            // Draw color circle
            colorPaint.color = colors[index]
            canvas.drawCircle(centerX, centerY, circleRadius, colorPaint)

            // Draw selection border and checkmark
            if (index == selectedIndex) {
                canvas.drawCircle(centerX, centerY, circleRadius + 4f, borderPaint)

                // Draw checkmark
                val checkSize = circleRadius * 0.5f
                val startX = centerX - checkSize * 0.3f
                val startY = centerY
                val midX = centerX
                val midY = centerY + checkSize * 0.3f
                val endX = centerX + checkSize * 0.5f
                val endY = centerY - checkSize * 0.3f

                canvas.drawLine(startX, startY, midX, midY, checkPaint)
                canvas.drawLine(midX, midY, endX, endY, checkPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            circleRects.forEachIndexed { index, rect ->
                val expandedRect = RectF(
                    rect.left - circleSpacing / 2,
                    rect.top - circleSpacing / 2,
                    rect.right + circleSpacing / 2,
                    rect.bottom + circleSpacing / 2
                )
                if (expandedRect.contains(event.x, event.y)) {
                    selectedIndex = index
                    onColorSelected?.invoke(colors[index])
                    invalidate()
                    performClick()
                    return true
                }
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredHeight = 80
        val height = resolveSize(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height)
    }
}
