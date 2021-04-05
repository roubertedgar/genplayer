package com.downstairs.genplayer.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.downstairs.genplayer.GenPlayer

object PlayerServiceConnection : ServiceConnection {

    private var onConnect: (GenPlayer) -> Unit = {}
    private var player: GenPlayer? = null

    fun connect(context: Context, onConnect: (GenPlayer) -> Unit) {
        this.onConnect = onConnect

        if (isConnected()) player?.also(onConnect) else bindService(context)
    }

    fun disconnect(context: Context) {
        context.unbindService(this)
        player = null
    }

    fun toForeground(context: Context) {
        ContextCompat.startForegroundService(context, getServiceIntent(context))
    }

    private fun bindService(context: Context) {
        context.startService(getServiceIntent(context))
        context.bindService(getServiceIntent(context), this, Context.BIND_AUTO_CREATE)
    }

    private fun getServiceIntent(context: Context) = Intent(context, PlayerService::class.java)

    override fun onServiceConnected(componentName: ComponentName?, serviceBinder: IBinder?) {
        (serviceBinder as? PlayerService.PLayerServiceBinder)?.also { binder ->

            player = binder.getPlayer()
            onConnect(player!!)
        }
    }

    override fun onServiceDisconnected(componentName: ComponentName?) {
        player = null
    }

    fun isConnected() = player != null
}