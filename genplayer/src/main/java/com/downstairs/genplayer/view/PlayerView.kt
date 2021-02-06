package com.downstairs.genplayer.view

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.downstairs.genplayer.R
import com.downstairs.genplayer.SplitPlayer
import com.downstairs.genplayer.content.Content
import com.downstairs.genplayer.databinding.PlayerViewBinding
import com.downstairs.genplayer.engine.EngineObserver
import com.downstairs.genplayer.engine.PlayerEngine
import com.downstairs.genplayer.service.PlayerServiceConnection
import com.google.android.gms.cast.framework.CastButtonFactory
import kotlinx.android.synthetic.main.player_controller_view.view.*
import kotlinx.android.synthetic.main.player_surface_layout.view.*
import kotlinx.android.synthetic.main.player_view.view.*


class PlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), LifecycleObserver {

    private val biding = PlayerViewBinding.bind(inflate(context, R.layout.player_view, this))

    private val serviceConnection = PlayerServiceConnection(context)
    private val activity: FragmentActivity?
        get() = context as? FragmentActivity

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        CastButtonFactory.setUpMediaRouteButton(context, castButton)
        setupListeners()
        biding.playerViewSurface.playerViewController.setTimeBar(biding.playerTimeBar)
    }

    private fun setupListeners() {
        biding.playerViewSurface.placeHolderControlView.setOnClickListener {
            biding.playerViewController.show()
        }
//        fullScreenButton.setOnClickListener {
//            activity?.also { FullScreenDialog(it).show(this) }
//        }
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
                biding.playerViewSurface.playerViewController.setPlayer(engine.player)
                playerViewSurface.setPlayer(engine)
            }
        })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        serviceConnection.disconnect()
    }
}

val PlayerViewBinding.playerViewController: PlayerControllerView
    get() = playerViewSurface.playerViewController

fun Context.convertDpToPixel(dp: Float): Float {
    return dp * (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Context.convertPixelsToDp(px: Float): Float {
    return px / (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}