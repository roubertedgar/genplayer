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
        PRIMARY,
        FINAL
    }

    private val attributes = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton)
    private val finalAnimatedDrawable = getDrawable(R.styleable.SwitchButton_drawable_final)
    private val initialAnimatedDrawable =
        getDrawable(R.styleable.SwitchButton_drawable_initial, finalAnimatedDrawable)

    private var onSwitch: (State) -> Unit = {}

    var state: State = State.PRIMARY
        set(value) {
            if (field != value) {
                field = value
                onSwitchStateChanged()
            }
        }

    init {
        chooseAnimatedDrawable()
    }

    fun setOnSwitchListener(onSwitch: (State) -> Unit) {
        this.onSwitch = onSwitch
    }

    override fun performClick(): Boolean {
        toggleState()
        return super.performClick()
    }

    private fun toggleState() {
        val state = if (state == State.PRIMARY) State.FINAL else State.PRIMARY
        this.state = state
    }

    private fun onSwitchStateChanged() {
        onSwitch(state)
        chooseAnimatedDrawable()
        startAnimation()
    }

    private fun chooseAnimatedDrawable() {
        val drawable =
            if (state == State.PRIMARY) initialAnimatedDrawable else finalAnimatedDrawable

        if (drawable != this.drawable) {
            setImageDrawable(drawable)
        }
    }

    private fun startAnimation() {
        (drawable as? AnimatedVectorDrawable)?.start()
    }

    private fun getDrawable(@StyleableRes id: Int, defaultValue: Drawable? = null): Drawable? {
        return attributes.getDrawable(id) ?: defaultValue
    }
}