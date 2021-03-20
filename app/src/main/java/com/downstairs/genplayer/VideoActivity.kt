package com.downstairs.genplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.downstairs.genplayer.content.HLSContentFactory
import com.downstairs.genplayer.view.PlayerView
import com.downstairs.genplayer.view.PlayerViewSurface

class VideoActivity : AppCompatActivity(R.layout.video_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val genPlayer = findViewById<PlayerView>(R.id.playerView)

        genPlayer.setLifecycleOwner(this)
        genPlayer.load(
            HLSContentFactory.createContent(
                "Eita nois",
                "https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8",
                mapOf()
            )
        )
    }
}