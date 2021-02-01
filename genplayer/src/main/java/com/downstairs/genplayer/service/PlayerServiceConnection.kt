package com.downstairs.genplayer.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.downstairs.genplayer.SplitPlayer

class PlayerServiceConnection(private val context: Context) : ServiceConnection {

    private var onConnect: (SplitPlayer) -> Unit = {}
    private var player: SplitPlayer? = null

    fun connect(onConnect: (SplitPlayer) -> Unit) {
        this.onConnect = onConnect

        if (isConnected()) player?.also(onConnect) else bindService()
    }

    fun disconnect() {
        context.unbindService(this)
    }

    private fun bindService() {
        ContextCompat.startForegroundService(context, getServiceIntent(context))
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

    private fun isConnected() = player != null
}