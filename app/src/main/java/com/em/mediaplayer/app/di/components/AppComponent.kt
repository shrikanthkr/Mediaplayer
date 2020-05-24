package com.em.mediaplayer.app.di.components

import android.app.Application
import com.em.db.SongsRepository
import com.em.mediaplayer.app.di.modules.AbstractApplicationModule
import com.em.mediaplayer.app.di.modules.ApplicationModule
import com.em.mediaplayer.app.di.modules.SubComponentsModule
import com.em.mediaplayer.app.di.modules.ViewModelModule
import com.em.mediaplayer.app.server.FileServer
import com.em.mediaplayer.player.PlayerController
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@ExperimentalStdlibApi
@FlowPreview
@ExperimentalCoroutinesApi
@Singleton
@Component(modules = [ApplicationModule::class, AbstractApplicationModule::class, SubComponentsModule::class, ViewModelModule::class])
interface AppComponent {

    fun fragmentComponent(): FragmentComponent.Factory
    fun activityComponent(): ActivityComponent.Factory
    fun controller(): PlayerController
    fun songsRepository(): SongsRepository
    fun fileServer(): FileServer

    @Component.Builder
    interface AppComponentBuilder {

        @BindsInstance
        fun application(app: Application): AppComponentBuilder

        @BindsInstance
        fun ioDispatcher(dispatcher: CoroutineDispatcher): AppComponentBuilder

        @BindsInstance
        fun globalScope(scope: CoroutineScope): AppComponentBuilder

        fun build(): AppComponent
    }

}