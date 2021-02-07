package com.downstairs.genplayer.view

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.downstairs.genplayer.R
import com.downstairs.genplayer.SplitPlayer
import com.downstairs.genplayer.content.Content
import com.downstairs.genplayer.databinding.PlayerViewSurfaceBinding
import com.downstairs.genplayer.engine.EngineObserver
import com.downstairs.genplayer.engine.PlayerEngine
import com.downstairs.genplayer.service.PlayerServiceConnection
import com.downstairs.genplayer.tools.Orientation
import com.google.android.gms.cast.framework.CastButtonFactory
import kotlinx.android.synthetic.main.player_controller_view.view.*
import kotlinx.android.synthetic.main.player_surface_layout.view.*


class PlayerViewSurface @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), LifecycleObserver {

    val biding = PlayerViewSurfaceBinding.bind(inflate(context, R.layout.player_view_surface, this))

    private val serviceConnection = PlayerServiceConnection(context)
    private var orientationListener: (Orientation) -> Unit = {}

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        CastButtonFactory.setUpMediaRouteButton(context, castButton)
        setupListeners()
        setupController()
    }

    private fun setupController() {
        biding.playerViewController.setTimeBar(biding.playerTimeBar)
    }

    private fun setupListeners() {
        biding.playerViewController.setListener(object : ControllerListener {
            override fun onOrientationRequested(orientation: Orientation) {
                orientationListener(orientation)
            }
        })

        biding.placeHolderControlView.setOnClickListener {
            biding.playerViewController.show()
        }
    }

    fun setOrientationListener(onChange: (Orientation) -> Unit) {
        this.orientationListener = onChange
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun load(vararg content: Content) {
        serviceConnection.connect { player ->
            bindView(player)
            player.addContent(content[0])
        }
    }

    private fun bindView(splitPlayer: SplitPlayer) {
        splitPlayer.addEngineListener(object : EngineObserver {
            override fun onEngineChanged(engine: PlayerEngine) {
                biding.playerViewController.setPlayer(engine.player)
                biding.surfaceView.setPlayer(engine)
            }
        })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        serviceConnection.disconnect()
    }
}

val PlayerViewSurfaceBinding.playerViewController: PlayerControllerView
    get() = surfaceView.playerViewController

val PlayerViewSurfaceBinding.placeHolderControlView: View
    get() = surfaceView.placeHolderControlView


fun Context.convertDpToPixel(dp: Float): Float {
    return dp * (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Context.convertPixelsToDp(px: Float): Float {
    return px / (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}