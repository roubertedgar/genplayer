package com.downstairs.genplayer.engine

import com.google.android.gms.cast.MediaQueueItem
import com.google.android.gms.cast.framework.CastContext

val CastContext.currentItem: MediaQueueItem?
    get() = sessionManager?.currentCastSession?.remoteMediaClient?.currentItem