package com.downstairs.genplayer

import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.downstairs.genplayer.content.Content
import kotlinx.android.synthetic.main.video_fragment.*

class VideoFragment : Fragment(R.layout.video_fragment), PictureInPictureFragment {

    override val wishEnterOnPipMode: Boolean
        get() = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        setupListeners()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }

            })

        playerView.setLifecycleOwner(this)

        getMediaListAdapter()?.submitList(
            listOf(
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
        )
    }

    private fun setupViews() {
        mediaList.adapter = MediaListAdapter()
        mediaList.layoutManager = LinearLayoutManager(context)
    }

    private fun setupListeners() {
        getMediaListAdapter()?.setOnItemClickListener {
            playerView.load(it)
        }
    }

    private fun getMediaListAdapter(): MediaListAdapter? {
        return mediaList.adapter as? MediaListAdapter
    }

    override fun enterOnPictureInPicture() {
        playerView.enterOnPictureInPictureMode()
        rootMotionLayout.transitionToEnd()
        startBackgroundTransition()
    }

    override fun exitFromPictureInPicture() {
        playerView.exitFromPictureInPictureMode()
        rootMotionLayout.transitionToStart()
        reverseBackgroundTransition()
    }

    private fun startBackgroundTransition() {
        (view?.background as? TransitionDrawable)?.startTransition(300)
    }

    private fun reverseBackgroundTransition() {
        (view?.background as? TransitionDrawable)?.reverseTransition(300)
    }
}