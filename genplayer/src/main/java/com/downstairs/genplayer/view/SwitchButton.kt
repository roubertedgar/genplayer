package com.downstairs.genplayer.view

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.StyleableRes
import androidx.appcompat.widget.AppCompatImageButton
import com.downstairs.genplayer.R


class SwitchButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageButton(context, attrs, defStyleAttr) {

    enum class State {
        START,
        END
    }

    private val attributes = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton)
    private val endAnimatedDrawable = getDrawable(R.styleable.SwitchButton_drawable_end)
    private val startAnimatedDrawable =
        getDrawable(R.styleable.SwitchButton_drawable_start, endAnimatedDrawable)

    private var onSwitch: (State) -> Unit = {}

    var state: State = State.START
        set(value) {
            if (field == value) return

            field = value
            chooseAnimatedDrawable()
            startAnimation()
        }

    init {
        isClickable = true
        isFocusable = true
        chooseAnimatedDrawable()
    }

    fun setOnSwitchListener(onSwitch: (State) -> Unit) {
        this.onSwitch = onSwitch
    }

    override fun performClick(): Boolean {
        switchState()
        return super.performClick()
    }

    private fun switchState() {
        val state = if (state == State.START) State.END else State.START
        this.state = state

        onSwitch(state)
    }

    private fun startAnimation() {
        val animatedVector = (drawable as? AnimatedVectorDrawable)
        animatedVector?.start()
    }

    private fun chooseAnimatedDrawable() {
        val drawable = if (state == State.START) startAnimatedDrawable else endAnimatedDrawable

        if (drawable != this.drawable) {
            setImageDrawable(drawable)
        }
    }

    private fun getDrawable(@StyleableRes id: Int, defaultValue: Drawable? = null): Drawable? {
        return attributes.getDrawable(id) ?: defaultValue
    }
}