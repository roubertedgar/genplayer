package com.downstairs.genplayer.view.components

import android.animation.ValueAnimator.INFINITE
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.animation.doOnCancel
import androidx.core.content.ContextCompat
import com.downstairs.genplayer.R

class PlaybackButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val size = context.resources.getDimension(R.dimen.default_progress_size)
    private val stroke = context.resources.getDimension(R.dimen.default_progress_stroke)
    private val colorAccent = ContextCompat.getColor(context, R.color.color_primary)

    private var progressBox = RectF(0f, 0f, 0f, 0f)
    private var progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = colorAccent }

    private var startAngle = 0.0f
    private var sweepAngle = 0.0f
    private var centerX = 0.0f
    private var centerY = 0.0f

    private val bufferingAnimation = animateIntValue(
        AnimationConfig(200, 1200, INFINITE, AccelerateInterpolator())
    ) { _, fraction ->
        if (fraction < 0.5) sweepAngle = 360 * fraction
        else if (fraction >= 0.5) sweepAngle = 360 - (360 * fraction)

        if (fraction >= 0.25) {
            startAngle = (480 * fraction) - 120
        }
        postInvalidate()
    }.apply {
        doOnCancel {
            startAngle = 0.0f
            sweepAngle = 0.0f
            postInvalidate()
        }
    }

    private var playback: Playback = Paused(context, progressBox)

    var state: PlaybackState = PlaybackState.Paused
        set(value) {
            if (field == value) return
            field = value
            updateState()
        }

    private var onStateChanged: (PlaybackState) -> Unit = {}

    init {
        if (isInEditMode) sweepAngle = 270f
    }

    fun setOnStateChangeListener(onChange: (PlaybackState) -> Unit) {
        this.onStateChanged = onChange
    }

    override fun performClick(): Boolean {
        state = if (state == PlaybackState.Playing) {
            PlaybackState.Paused
        } else {
            PlaybackState.Playing
        }

        return super.performClick()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredHeight = size.toInt()
        val desiredWidth = size.toInt()

        val measuredWidth = measureDimension(desiredWidth, widthMeasureSpec)
        val measuredHeight = measureDimension(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        centerX = (width / 2f)
        centerY = height / 2f
        updateProgressBoxSize(width, height)
        updateState()
    }

    private fun updateState() {
        playback = when (state) {
            PlaybackState.Playing -> Playing(context, progressBox)
            PlaybackState.Paused -> Paused(context, progressBox)
            PlaybackState.Buffering -> Empty(context, progressBox)
        }

        isClickable = state.isClickable
        isFocusable = state.isClickable

        if (state is PlaybackState.Buffering) bufferingAnimation.start() else bufferingAnimation.cancel()
        if (!progressBox.isEmpty) postInvalidate()

        onStateChanged(state)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.save()
        canvas.rotate(-90f, centerX, centerY)
        canvas.drawArc(progressBox, startAngle, sweepAngle, false, progressPaint)
        canvas.restore()

        canvas.drawPath(playback.path, playback.paint)
    }

    fun setProgress(progress: Int, duration: Long = 100) {
        val angle = progress * 360 / 100
        val config = AnimationConfig(angle, duration)

        animateIntValue(config) { animated, _ ->
            sweepAngle = animated.toFloat()
            postInvalidate()
        }
    }

    private fun updateProgressBoxSize(width: Int, height: Int) {
        val halfStroke = stroke / 2

        val left = halfStroke + paddingLeft
        val right = (width - halfStroke) - paddingRight
        val bottom = (height - halfStroke) - paddingBottom
        val top = halfStroke + paddingTop

        progressBox = RectF(left, top, right, bottom)
        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeWidth = stroke
    }
}

