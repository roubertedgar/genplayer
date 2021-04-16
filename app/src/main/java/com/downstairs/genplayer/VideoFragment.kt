package com.downstairs.genplayer

import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.downstairs.genplayer.content.Content
import com.downstairs.genplayer.view.setVerticalBias
import kotlinx.android.synthetic.main.video_fragment.*


class VideoFragment : Fragment(R.layout.video_fragment), PictureInPictureFragment {

    override val wishEnterOnPipMode: Boolean
        get() = true

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
        if (isInPictureInPictureMode) {
            enterOnPictureInPicture()
        } else {
            exitFromPictureInPicture()
        }
    }

    override fun enterOnPictureInPicture() {
        startBackgroundTransition()
        playerView.setVerticalBias(0.5f)
        playerView.enterOnPictureInPictureMode()
    }

    override fun exitFromPictureInPicture() {
        reverseBackgroundTransition()
        playerView.setVerticalBias(0f)
        playerView.exitFromPictureInPictureMode()
    }

    private fun startBackgroundTransition() {
        (view?.background as? TransitionDrawable)?.startTransition(200)
    }

    private fun reverseBackgroundTransition() {
        (view?.background as? TransitionDrawable)?.reverseTransition(300)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        playerView.setLifecycleOwner(this)
        playerView.load(
            Content(
                "Eita nois",
                "Fodasse",
                "https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd",
                "",
                "application/dash+xml",
                emptyMap(),
                0
            )
        )
    }
}