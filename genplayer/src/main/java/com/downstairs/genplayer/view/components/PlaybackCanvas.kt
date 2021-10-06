package com.downstairs.genplayer.view.components

import android.content.Context
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import androidx.core.content.ContextCompat
import com.downstairs.genplayer.R
import kotlin.math.sqrt

class Playing(context: Context, box: RectF) : Playback(context, box) {
    override val path = Path().apply {
        val left = rect.left + (rect.right * 0.15f)
        val right = rect.right - (rect.right * 0.15f)

        moveTo(left, rect.top)
        lineTo(left, rect.bottom)
        moveTo(right, rect.top)
        lineTo(right, rect.bottom)
    }

    override val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = ContextCompat.getColor(context, R.color.color_primary)
        strokeWidth = context.dpToPixel(6f)
    }
}

class Paused(context: Context, box: RectF) : Playback(context, box) {
    override val path = Path().apply {
        val left = rect.left + (rect.right * 0.15f)

        moveTo(left, rect.top)
        lineTo(left, rect.bottom)
        lineTo(rect.right, rect.centerY())
        lineTo(left, rect.top)
        close()
    }

    override val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.color_primary)
    }
}

class Empty(context: Context, box: RectF) : Playback(context, box) {
    override val path = Path()
    override val paint = Paint()
}

abstract class Playback(context: Context, box: RectF) {
    val rect: RectF
    abstract val path: Path
    abstract val paint: Paint

    init {
        val playbackPadding = context.dpToPixel(6f)

        val x = (box.right - box.centerX()) / sqrt(2.0).toFloat()
        val y = (box.bottom - box.centerY()) / sqrt(2.0).toFloat()

        val left = box.centerX() - x + playbackPadding
        val right = box.centerX() + x - playbackPadding
        val top = box.centerY() - y + playbackPadding
        val bottom = box.centerY() + y - playbackPadding
        rect = RectF(left, top, right, bottom)
    }
}