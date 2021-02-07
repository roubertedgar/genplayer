package com.downstairs.genplayer.view

import com.google.android.exoplayer2.ui.DefaultTimeBar

fun DefaultTimeBar.moveToBottom() {
    translationY = context.convertDpToPixel(11f)
    setPadding(
        context.convertDpToPixel(-8f).toInt(), 0,
        context.convertDpToPixel(-8f).toInt(), 0
    )
}

fun DefaultTimeBar.resetPosition() {
    translationY = context.convertDpToPixel(-11f)
    setPadding(
        context.convertDpToPixel(16f).toInt(), 0,
        context.convertDpToPixel(16f).toInt(), 0
    )
}