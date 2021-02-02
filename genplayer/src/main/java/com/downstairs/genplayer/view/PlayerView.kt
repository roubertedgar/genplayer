package com.downstairs.genplayer.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.downstairs.genplayer.R
import com.downstairs.genplayer.SplitPlayer
import com.downstairs.genplayer.content.Content
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

    private val serviceConnection = PlayerServiceConnection(context)
    private val activity: FragmentActivity?
        get() = context as? FragmentActivity

    init {
        inflate(context, R.layout.player_view, this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        CastButtonFactory.setUpMediaRouteButton(context, castButton)
        setupListeners()
    }

    private fun setupListeners() {
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
                playerViewSurface.setPlayer(engine)
            }
        })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        serviceConnection.disconnect()
    }
}