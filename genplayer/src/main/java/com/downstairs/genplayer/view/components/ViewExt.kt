package com.downstairs.genplayer.view.components

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnPause

fun measureDimension(desiredSize: Int, measureSpec: Int): Int {

    val specMode = View.MeasureSpec.getMode(measureSpec)
    val specSize = View.MeasureSpec.getSize(measureSpec)

    if (specMode == View.MeasureSpec.EXACTLY) {
        return specSize
    } else {

        if (specMode == View.MeasureSpec.AT_MOST) {
            return desiredSize.coerceAtMost(specSize)
        }

        return desiredSize
    }
}


fun Context.pixelToDp(pixel: Float): Float {
    val scale = this.resources.displayMetrics.density
    return pixel / scale - 0.5f
}

fun Context.dpToPixel(dp: Float): Float {
    val scale = this.resources.displayMetrics.density
    return dp * scale + 0.5f
}

fun View.pixelToDp(pixel: Float): Float {
    val scale = this.resources.displayMetrics.density
    return pixel / scale - 0.5f
}

fun View.dpToPixel(dp: Float): Float {
    val scale = this.resources.displayMetrics.density
    return dp * scale + 0.5f
}

fun animateIntValue(
    config: AnimationConfig,
    onUpdate: (value: Int, fraction: Float) -> Unit
): ValueAnimator {
    return ValueAnimator.ofInt(0, config.value).apply {
        duration = config.duration
        repeatCount = config.repeat
        interpolator = config.interpolator
        addUpdateListener { valueAnimator ->
            onUpdate(
                valueAnimator.animatedValue as Int,
                valueAnimator.animatedFraction
            )
        }
        doOnPause { }
    }
}

data class AnimationConfig(
    val value: Int = 100,
    val duration: Long = 600,
    val repeat: Int = 0,
    val interpolator: Interpolator = LinearInterpolator()
)