package com.downstairs.genplayer.view

import androidx.core.view.isVisible
import com.google.android.exoplayer2.ui.DefaultTimeBar

fun DefaultTimeBar.moveToBottom() {
    translationY = context.convertDpToPixel(-2f)
    setPadding(
        context.convertDpToPixel(-8f).toInt(), 0,
        context.convertDpToPixel(-8f).toInt(), 0
    )
}

fun DefaultTimeBar.resetPosition() {
    translationY = context.convertDpToPixel(-20f)
    setPadding(
        context.convertDpToPixel(20f).toInt(), 0,
        context.convertDpToPixel(20f).toInt(), 0
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