package com.downstairs.genplayer.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.downstairs.genplayer.R
import com.downstairs.genplayer.databinding.MediaButtonsViewBinding
import com.downstairs.genplayer.databinding.MediaControlViewBinding
import com.downstairs.genplayer.tools.orientation.Orientation
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.TimeBar

class MediaControl @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var control: MediaControlViewBinding
    private var buttons: MediaButtonsViewBinding

    private var hideTimer = ViewTimer()
    private var onCommand: (Command) -> Unit = {}

    init {
        inflate(context, R.layout.media_control_view, this)

        control = MediaControlViewBinding.bind(rootView)
        buttons = control.mediaButtons
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupListeners()
        if (isInPortrait) switchToPortrait() else switchToFullScreen()
    }

    private val onClickListener: (View) -> Unit = { view ->
        when (view) {
            buttons.fastForward -> onCommand(Command.Forward)
            buttons.rewind -> onCommand(Command.Rewind)
        }

        setControlVisibility(isVisible = true)
    }

    private fun setupListeners() {
        buttons.playback.setOnClickListener(onClickListener)
        buttons.fastForward.setOnClickListener(onClickListener)
        buttons.rewind.setOnClickListener(onClickListener)
        control.orientation.setOnClickListener(onClickListener)

        buttons.playback.setOnSwitchListener { playbackSwitched(it) }
        control.orientation.setOnSwitchListener { orientationSwitched(it) }

        control.root.setOnClickListener { setControlVisibility(isVisible = !isControlVisible) }
        control.timeBar.onScrub { progress -> onCommand(Command.Seek(progress)) }
    }

    fun setListener(onCommand: (Command) -> Unit) {
        this.onCommand = onCommand
    }

    fun play() {
        buttons.playback.state = SwitchButton.State.FINAL
    }

    fun pause() {
        buttons.playback.state = SwitchButton.State.PRIMARY
    }

    fun isLoading(isLoading: Boolean = false) {
        //buttons.playback.state  = isLoading
    }

    fun updateProgress(currentPosition: Long, bufferedPosition: Long, duration: Long) {
        control.timeBar.setPosition(currentPosition)
        control.timeBar.setBufferedPosition(bufferedPosition)
        control.timeBar.setDuration(duration)
    }

    private fun setControlVisibility(isVisible: Boolean) {
        val isTimeBarVisible = if (isInPortrait) true else isVisible
        val frameVisibility = if (isVisible) VISIBLE else INVISIBLE
        if (isVisible) hideAfterTimeout() else hideTimer.cancel()

        control.frame.visibility = frameVisibility
        buttons.playback.isVisible = isVisible
        buttons.fastForward.isVisible = isVisible
        buttons.rewind.isVisible = isVisible
        control.orientation.isVisible = isVisible
        control.timeBar.isVisible = isTimeBarVisible
        control.timeBar.isScrubVisible = isVisible
    }

    private fun hideAfterTimeout() {
        if (isAttachedToWindow) {
            hideTimer.schedule(DEFAULT_HIDE_DELAY_MS) { setControlVisibility(false) }
        }
    }

    private fun switchToFullScreen() {
        control.bottomContainer.moveY(0f)
        control.timeBar.horizontalPadding(16f)
        control.timeBarPlaceHolder.isVisible = false
    }

    private fun switchToPortrait() {
        control.bottomContainer.moveY(11f)
        control.timeBar.horizontalPadding(-8f)
        control.timeBarPlaceHolder.isVisible = true
    }

    private fun playbackSwitched(state: SwitchButton.State) {
        if (state == SwitchButton.State.FINAL) {
            onCommand(Command.Playback(isPlaying = true))
        } else {
            onCommand(Command.Playback(isPlaying = false))
        }
    }

    private fun orientationSwitched(state: SwitchButton.State) {
        if (state == SwitchButton.State.PRIMARY) {
            onCommand(Command.Rotate(Orientation.PORTRAIT))
        } else {
            onCommand(Command.Rotate(Orientation.LANDSCAPE))
        }
    }

    private val isInPortrait get() = control.orientation.state == SwitchButton.State.PRIMARY
    private val isControlVisible get() = control.frame.isVisible

    companion object {
        private const val DEFAULT_HIDE_DELAY_MS = 5000L
    }
}

class ScrubListener(
    val onStart: (Long) -> Unit = {},
    val onMove: (Long) -> Unit = {},
    val onStop: (Long) -> Unit = {}
) : TimeBar.OnScrubListener {
    override fun onScrubStart(timeBar: TimeBar, position: Long) {
        onStart(position)
    }

    override fun onScrubMove(timeBar: TimeBar, position: Long) {
        onMove(position)
    }

    override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
        if (!canceled) {
            onStop(position)
        }
    }
}

var DefaultTimeBar.isScrubVisible: Boolean
    get() = isVisible
    set(value) = if (value) showScrubber(300L) else hideScrubber(300L)

fun DefaultTimeBar.onScrub(
    start: (position: Long) -> Unit = {},
    move: (position: Long) -> Unit = {},
    stop: (position: Long) -> Unit = {}
) {
    this.addListener(ScrubListener(start, move, stop))
}

sealed class Command {
    object Rewind : Command()
    object Forward : Command()
    data class Seek(val position: Long) : Command()
    data class Playback(val isPlaying: Boolean) : Command()
    data class Rotate(val orientation: Orientation) : Command()
}