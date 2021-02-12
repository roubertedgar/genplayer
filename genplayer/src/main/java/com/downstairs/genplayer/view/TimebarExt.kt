package com.downstairs.genplayer.view

import androidx.annotation.Px
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.downstairs.genplayer.R
import com.google.android.exoplayer2.ui.DefaultTimeBar

fun DefaultTimeBar.toFullScreenConstraints() {
    moveY(-20f)
    horizontalPadding(20f)
}

fun DefaultTimeBar.toPortraitConstraints() {
    moveY(-2f)
    horizontalPadding(-8f)
}

fun DefaultTimeBar.moveY(dp: Float) {
    translationY = context.dpToPixel(dp)
}

fun DefaultTimeBar.horizontalPadding(dp: Float) {
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

fun DefaultTimeBar.hide(hideAll: Boolean) {
    if (hideAll) {
        isVisible = false
    }

    hideScrubber(300L)
}

fun DefaultTimeBar.show() {
    isVisible = true
    showScrubber(300L)
}

fun ConstraintLayout.setBottomMargin(@Px margin: Float) {
    val params = layoutParams as ConstraintLayout.LayoutParams
    params.bottomMargin = margin.toInt()
}