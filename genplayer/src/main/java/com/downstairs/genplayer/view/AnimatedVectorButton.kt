package com.downstairs.genplayer.view

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.StyleableRes
import androidx.appcompat.widget.AppCompatImageButton
import com.downstairs.genplayer.R


class AnimatedVectorButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageButton(context, attrs, defStyleAttr) {

    enum class State {
        STATE_START,
        STATE_END
    }

    var isChecked = false

    private val attributes = context.obtainStyledAttributes(attrs, R.styleable.AnimatedVectorButton)

    private val endAnimatedDrawable =
        getDrawable(R.styleable.AnimatedVectorButton_end_animated_drawable)
    private val startAnimatedDrawable =
        getDrawable(R.styleable.AnimatedVectorButton_start_animated_drawable) ?: endAnimatedDrawable

    init {
        chooseAnimatedDrawable()
    }

    fun moveToState(state: State) {
        setState(state)
        startAnimation()
    }

    private fun setState(state: State) {
        isChecked = when (state) {
            State.STATE_START -> false
            State.STATE_END -> true
        }

        chooseAnimatedDrawable()
    }

    private fun startAnimation() {
        val animatedVector = (drawable as? AnimatedVectorDrawable)
        animatedVector?.start()
    }

    private fun chooseAnimatedDrawable() {
        val drawable = if (isChecked) endAnimatedDrawable else startAnimatedDrawable

        if (drawable != this.drawable) {
            setImageDrawable(drawable)
        }
    }

    private fun getDrawable(@StyleableRes id: Int): Drawable? {
        return attributes.getDrawable(id)
    }
}