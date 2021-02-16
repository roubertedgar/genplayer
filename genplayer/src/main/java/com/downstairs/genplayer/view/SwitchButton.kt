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
        INITIAL,
        FINAL
    }

    private val attributes = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton)
    private val finalAnimatedDrawable = getDrawable(R.styleable.SwitchButton_drawable_final)
    private val initialAnimatedDrawable =
        getDrawable(R.styleable.SwitchButton_drawable_initial, finalAnimatedDrawable)

    private var onSwitch: (State) -> Unit = {}

    var state: State = State.INITIAL
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
        val state = if (state == State.INITIAL) State.FINAL else State.INITIAL
        this.state = state

        onSwitch(state)
    }

    private fun startAnimation() {
        val animatedVector = (drawable as? AnimatedVectorDrawable)
        animatedVector?.start()
    }

    private fun chooseAnimatedDrawable() {
        val drawable = if (state == State.INITIAL) initialAnimatedDrawable else finalAnimatedDrawable

        if (drawable != this.drawable) {
            setImageDrawable(drawable)
        }
    }

    private fun getDrawable(@StyleableRes id: Int, defaultValue: Drawable? = null): Drawable? {
        return attributes.getDrawable(id) ?: defaultValue
    }
}