package com.downstairs.genplayer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.downstairs.genplayer.content.Content
import com.downstairs.genplayer.content.HLSContentFactory
import kotlinx.android.synthetic.main.video_fragment.*

class VideoFragment : Fragment(R.layout.video_fragment), PictureInPictureFragment {

    override val wishEnterOnPipMode: Boolean
        get() = true

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
        if (isInPictureInPictureMode) {
            playerView.disableControls()
        } else {
            playerView.enableControls()
        }
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