package com.downstairs.genplayer.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.downstairs.genplayer.R
import com.downstairs.genplayer.content.Content
import com.downstairs.genplayer.databinding.PlayerViewBinding
import com.downstairs.genplayer.tools.Orientation

class PlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val biding = PlayerViewBinding.bind(inflate(context, R.layout.player_view, this))
    private val fullScreenDialog: FullScreenDialog = FullScreenDialog(context)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        biding.playerViewSurface.setOrientationListener { orientation ->
            onOrientationChanged(orientation)
        }
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        biding.playerViewSurface.setLifecycleOwner(lifecycleOwner)
    }

    private fun onOrientationChanged(orientation: Orientation) {
        if (orientation == Orientation.LANDSCAPE) {
            fullScreenDialog.show(biding.playerViewSurface)
        } else {
            fullScreenDialog.dismiss()
            addView(biding.playerViewSurface)
        }
    }

    fun load(vararg content: Content) {
        biding.playerViewSurface.load(*content)
    }
}