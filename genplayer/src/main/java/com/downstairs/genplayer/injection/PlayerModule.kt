package com.downstairs.genplayer.injection

import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import com.downstairs.genplayer.session.PlayerMediaSession
import com.downstairs.genplayer.content.CustomHeaderMediaSourceFactory
import com.downstairs.genplayer.tools.ArtworkLoader
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PlayerModule {

    companion object {
        const val MEDIA_SESSION_TAG = "MediaSessionPlayerTag"
    }

    @Singleton
    @Provides
    fun providesMediaSession(context: Context, loader: ArtworkLoader): PlayerMediaSession {
        return PlayerMediaSession(context, MediaSessionCompat(context, MEDIA_SESSION_TAG), loader)
    }

    @Singleton
    @Provides
    fun providesExoplayer(context: Context): SimpleExoPlayer {
        return SimpleExoPlayer.Builder(context)
            .setMediaSourceFactory(CustomHeaderMediaSourceFactory())
            .build()
    }
}