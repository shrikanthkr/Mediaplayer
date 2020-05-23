package com.em.app.di.components

import android.app.Application
import com.em.app.di.modules.AbstractApplicationModule
import com.em.app.di.modules.ApplicationModule
import com.em.app.di.modules.SubComponentsModule
import com.em.app.di.modules.ViewModelModule
import com.em.db.SongsRepository
import com.em.player.PlayerController
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@Singleton
@Component(modules = [ApplicationModule::class, AbstractApplicationModule::class, SubComponentsModule::class, ViewModelModule::class])
interface AppComponent {

    fun fragmentComponent(): FragmentComponent.Factory
    fun activityComponent(): ActivityComponent.Factory
    fun controller(): PlayerController
    fun songsRepository(): SongsRepository

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