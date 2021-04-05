package com.downstairs.genplayer.injection

import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import com.downstairs.genplayer.content.CustomHeaderMediaSourceFactory
import com.downstairs.genplayer.service.PlayerServiceConnection
import com.downstairs.genplayer.session.MediaSessionReceiver
import com.downstairs.genplayer.session.PlayerMediaSession
import com.downstairs.genplayer.tools.ArtworkLoader
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PlayerModule {

    @Singleton
    @Provides
    fun providesMediaSessionCompat(context: Context) =
        MediaSessionCompat(context, MEDIA_SESSION_TAG)

    @Singleton
    @Provides
    fun providesMediaSession(
        mediaSessionCompat: MediaSessionCompat,
        mediaSessionReceiver: MediaSessionReceiver,
        artworkLoader: ArtworkLoader
    ) = PlayerMediaSession(mediaSessionCompat, mediaSessionReceiver, artworkLoader)

    @Singleton
    @Provides
    fun providesExoplayer(context: Context): SimpleExoPlayer {
        return SimpleExoPlayer.Builder(context)
            .setMediaSourceFactory(CustomHeaderMediaSourceFactory())
            .build()
    }

    @Singleton
    @Provides
    fun providesPlayerServiceConnection(context: Context): PlayerServiceConnection {
        return PlayerServiceConnection(context)
    }

    companion object {
        const val MEDIA_SESSION_TAG = "GenPlayerMediaSession"
    }
}