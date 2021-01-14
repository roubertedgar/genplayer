package com.downstairs.genplayer.injection

import android.content.Context
import com.downstairs.genplayer.PlayerMediaSession
import com.downstairs.genplayer.content.CustomHeaderMediaSourceFactory
import com.downstairs.genplayer.tools.ArtworkLoader
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PlayerModule {

    @Singleton
    @Provides
    fun providesMediaSession(context: Context, loader:ArtworkLoader): PlayerMediaSession {
        return PlayerMediaSession(context, loader)
    }

    @Singleton
    @Provides
    fun providesExoplayer(context: Context): SimpleExoPlayer {
        return SimpleExoPlayer.Builder(context)
            .setMediaSourceFactory(CustomHeaderMediaSourceFactory())
            .build()
    }
}