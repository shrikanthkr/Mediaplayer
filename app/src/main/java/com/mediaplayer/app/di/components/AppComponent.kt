package com.mediaplayer.app.di.components

import android.app.Application
import com.mediaplayer.app.di.modules.AbstractApplicationModule
import com.mediaplayer.app.di.modules.ApplicationModule
import com.mediaplayer.app.di.modules.SubComponentsModule
import com.mediaplayer.app.di.modules.ViewModelModule
import com.mediaplayer.player.PlayerController
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton


@Singleton
@Component(modules = [ApplicationModule::class, AbstractApplicationModule::class, SubComponentsModule::class, ViewModelModule::class])
interface AppComponent {

    fun fragmentComponent(): FragmentComponent.Factory
    fun activityComponent(): ActivityComponent.Factory
    fun controller(): PlayerController
    @Component.Builder
    interface AppComponentBuilder {

        @BindsInstance
        fun application(app: Application): AppComponentBuilder

        @BindsInstance
        fun ioDispatcher(dispatcher: CoroutineDispatcher): AppComponentBuilder

        fun build(): AppComponent
    }

}