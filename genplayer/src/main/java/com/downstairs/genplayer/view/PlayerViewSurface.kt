package com.downstairs.genplayer.view

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.downstairs.genplayer.GenPlayer
import com.downstairs.genplayer.R
import com.downstairs.genplayer.content.Content
import com.downstairs.genplayer.engine.EngineObserver
import com.downstairs.genplayer.engine.PlayerEngine
import com.downstairs.genplayer.service.PlayerServiceConnection
import com.downstairs.genplayer.tools.orientation.Orientation
import kotlinx.android.synthetic.main.player_view_surface.view.*

class PlayerViewSurface @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), LifecycleObserver {

    private val fullScreenDialog: FullScreenDialog = FullScreenDialog(context)

    init {
        inflate(context, R.layout.player_view_surface, this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        setupListeners()
    }

    private fun setupListeners() {
        playerViewController.setListener(object : ControllerListener {
            override fun onOrientationRequested(orientation: Orientation) {
                this@PlayerViewSurface.onOrientationChanged(orientation)
            }
        })
    }

    private fun onOrientationChanged(orientation: Orientation) {
        if (orientation == Orientation.LANDSCAPE) {
            fullScreenDialog.show(playerViewContainer)
        } else {
            fullScreenDialog.dismiss()
            addView(playerViewContainer)
        }
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun load(vararg content: Content) {
        PlayerServiceConnection.connect(context) { player ->
            bindView(player)
            player.addContent(content[0])
        }
    }

    fun enableControls() {
        playerViewController.enable()
    }

    fun disableControls() {
        playerViewController.disable()
    }

    private fun bindView(genPlayer: GenPlayer) {
        genPlayer.addEngineListener(object : EngineObserver() {
            override fun onEngineChanged(engine: PlayerEngine) {
                playerViewController.setPlayer(engine.player)
                surfaceView.setPlayer(engine)
                surfaceView.setAspectRatioListener { targetAspectRatio, naturalAspectRatio, aspectRatioMismatch ->
                    print("$targetAspectRatio, $naturalAspectRatio, $aspectRatioMismatch")
                }
            }
        })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        PlayerServiceConnection.disconnect(context)
    }
}

fun Context.dpToPixel(dp: Float): Float {
    return dp * (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Context.pixelToDp(px: Float): Float {
    return px / (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}