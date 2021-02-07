package com.downstairs.genplayer.view

import androidx.core.view.isVisible
import com.google.android.exoplayer2.ui.DefaultTimeBar

fun DefaultTimeBar.moveToBottom() {
    translationY = context.convertDpToPixel(-11f)
    setPadding(
        context.convertDpToPixel(-8f).toInt(), 0,
        context.convertDpToPixel(-8f).toInt(), 0
    )
}

fun DefaultTimeBar.resetPosition() {
    translationY = context.convertDpToPixel(-22f)
    setPadding(
        context.convertDpToPixel(16f).toInt(), 0,
        context.convertDpToPixel(16f).toInt(), 0
    )
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