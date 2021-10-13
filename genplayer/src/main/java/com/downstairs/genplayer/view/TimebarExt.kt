package com.downstairs.genplayer.view

import android.view.View
import androidx.annotation.Px
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.downstairs.genplayer.R
import com.downstairs.genplayer.view.components.dpToPixel
import com.google.android.exoplayer2.ui.DefaultTimeBar

fun View.moveY(dp: Float) {
    translationY = context.dpToPixel(dp)
}

fun View.horizontalPadding(dp: Float) {
    setPadding(
        context.dpToPixel(dp).toInt(), 0,
        context.dpToPixel(dp).toInt(), 0
    )
}

val DefaultTimeBar.barTopHeight: Float
    get() {
        val barHeight = context.resources.getDimension(R.dimen.timeline_bar_height)
        val touchHeight = context.resources.getDimension(R.dimen.timeline_touch_height)
        return touchHeight / 2 + barHeight
    }

fun DefaultTimeBar.hideControls() {
    isVisible = false
}

fun DefaultTimeBar.showControls() {
    isVisible = true
    showScrubber(300L)
}

fun ConstraintLayout.setBottomMargin(@Px margin: Float) {
    val params = layoutParams as ConstraintLayout.LayoutParams
    params.bottomMargin = margin.toInt()
}