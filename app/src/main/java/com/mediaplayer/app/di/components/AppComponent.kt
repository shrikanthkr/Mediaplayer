package com.mediaplayer.app.di.components

import android.app.Application
import com.mediaplayer.app.di.modules.SubComponentsModule
import com.mediaplayer.app.di.modules.ViewModelModule
import com.mediaplayer.db.SongsRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [SubComponentsModule::class, ViewModelModule::class])
interface AppComponent {

    fun songsRepository(): SongsRepository
    fun fragmentComponent(): FragmentComponent.Factory

    @Component.Builder
    interface AppComponentBuilder {

        @BindsInstance
        fun application(app: Application): AppComponentBuilder

        fun build(): AppComponent
    }

}