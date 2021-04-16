package com.downstairs.genplayer

import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.fragment.app.Fragment

interface PictureInPictureFragment {

    val wishEnterOnPipMode: Boolean

    fun enterOnPictureInPicture()

    fun exitFromPictureInPicture()
}

fun Fragment.getThemeColor(@AttrRes colorRes: Int): Int {
    var color = 0

    context?.also {
        TypedValue().run {
            val colors = it.obtainStyledAttributes(data, intArrayOf(colorRes))
            color = colors.getColor(0, 0)
            colors.recycle()
        }
    }

    return color
}
