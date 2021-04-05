package com.downstairs.genplayer.injection

import android.content.Context
import com.downstairs.genplayer.service.PlayerService
import com.downstairs.genplayer.view.PlayerViewSurface
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [PlayerModule::class])
interface PlayerComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): PlayerComponent
    }

    fun inject(service: PlayerService)

    fun inject(view: PlayerViewSurface)
}