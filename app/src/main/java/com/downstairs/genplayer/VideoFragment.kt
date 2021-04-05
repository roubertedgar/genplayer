package com.downstairs.genplayer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
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
            HLSContentFactory.createContent(
                "Eita nois",
                "https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8",
                mapOf()
            )
        )
    }
}