package com.downstairs.genplayer.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import com.downstairs.genplayer.R
import com.downstairs.genplayer.content.Content
import com.downstairs.genplayer.tools.orientation.Orientation
import kotlinx.android.synthetic.main.player_view.view.*

class PlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val fullScreenDialog: FullScreenDialog = FullScreenDialog(context)

    init {
        inflate(context, R.layout.player_view, this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        playerViewSurface.setOrientationListener { orientation ->
            onOrientationChanged(orientation)
        }
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        playerViewSurface.setLifecycleOwner(lifecycleOwner)
    }

    private fun onOrientationChanged(orientation: Orientation) {
        if (orientation == Orientation.LANDSCAPE) {
            fullScreenDialog.show(playerViewSurface)
        } else {
            fullScreenDialog.dismiss()
            addView(playerViewSurface)
        }
    }

    fun load(vararg content: Content) {
        playerViewSurface.load(*content)
    }
}