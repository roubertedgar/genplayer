package com.downstairs.genplayer.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.downstairs.genplayer.GenPlayer
import com.downstairs.genplayer.R
import com.downstairs.genplayer.content.Content
import com.downstairs.genplayer.databinding.PlayerViewSurfaceBinding
import com.downstairs.genplayer.engine.EngineObserver
import com.downstairs.genplayer.engine.PlayerEngine
import com.downstairs.genplayer.service.PlayerServiceConnection
import com.downstairs.genplayer.tools.orientation.Orientation

class PlayerViewSurface @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), LifecycleObserver {

    private val fullScreenDialog: FullScreenDialog = FullScreenDialog(context)
    private val binding: PlayerViewSurfaceBinding

    init {
        inflate(context, R.layout.player_view_surface, this)
        binding = PlayerViewSurfaceBinding.bind(rootView)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupListeners()
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

    private fun setupListeners() {
        binding.playerViewController.setListener(object : ControllerListener {
            override fun onOrientationChanged(orientation: Orientation) {
                this@PlayerViewSurface.onOrientationChanged(orientation)
            }
        })
    }

    private fun onOrientationChanged(orientation: Orientation) {
        if (orientation == Orientation.LANDSCAPE && !fullScreenDialog.isShowing) {
            fullScreenDialog.show(binding.playerViewContainer)
        } else if (fullScreenDialog.isShowing) {
            fullScreenDialog.dismiss()
            addView(binding.playerViewContainer)
        }
    }

    fun enterOnPictureInPictureMode() {
        binding.playerViewController.toPortraitMode()
        binding.playerViewController.disable()
    }

    fun exitFromPictureInPictureMode() {
        binding.playerViewController.enable()
    }

    private fun bindView(genPlayer: GenPlayer) {
        genPlayer.addEngineListener(object : EngineObserver() {
            override fun onEngineChanged(engine: PlayerEngine) {
                binding.playerViewController.setPlayer(engine.player)
                binding.surfaceView.setPlayer(engine)
            }
        })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        PlayerServiceConnection.disconnect(context)
    }
}