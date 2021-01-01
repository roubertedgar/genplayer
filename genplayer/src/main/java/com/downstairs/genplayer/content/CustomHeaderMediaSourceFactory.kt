@file:JvmName("CustomHeaderMediaItemConverterKt")

package com.downstairs.genplayer.content

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.drm.DrmSessionManager
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy

class CustomHeaderMediaSourceFactory : MediaSourceFactory {

    override fun setDrmSessionManager(drmSessionManager: DrmSessionManager?): MediaSourceFactory {
        return this
    }

    override fun setDrmHttpDataSourceFactory(drmHttpDataSourceFactory: HttpDataSource.Factory?): MediaSourceFactory {
        return this
    }

    override fun setDrmUserAgent(userAgent: String?): MediaSourceFactory {
        return this
    }

    override fun setLoadErrorHandlingPolicy(loadErrorHandlingPolicy: LoadErrorHandlingPolicy?): MediaSourceFactory {
        return this
    }

    override fun getSupportedTypes(): IntArray {
        return intArrayOf()
    }

    override fun createMediaSource(mediaItem: MediaItem): MediaSource {
        val properties = mediaItem.properties
            .filter { it.key == MediaProperty.COOKIE || it.key == MediaProperty.COOKIES }
            .mapKeys { it.key.value }

        val dataSource = DefaultHttpDataSourceFactory().apply {
            defaultRequestProperties.set(properties)
        }

        return DefaultMediaSourceFactory(dataSource).createMediaSource(mediaItem)
    }
}