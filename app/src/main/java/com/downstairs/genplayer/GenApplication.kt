package com.downstairs.genplayer

import android.app.Application
import com.google.android.gms.cast.framework.CastContext

class GenApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        CastContext.getSharedInstance(this)
    }
}
