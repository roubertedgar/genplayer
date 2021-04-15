package com.downstairs.genplayer.view

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout

fun View.removeParent() {
    (parent as ViewGroup).removeView(this)
}

fun View.setVerticalBias(vertical: Float) {
    (layoutParams as? ConstraintLayout.LayoutParams)?.also { params ->
        params.verticalBias = vertical
        layoutParams = params
    }
}