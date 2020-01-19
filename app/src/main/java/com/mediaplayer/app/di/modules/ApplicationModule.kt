package com.mediaplayer.app.di.modules

import android.app.Application
import com.mediaplayer.db.SongsRepository
import com.mediaplayer.player.PlayerAdapter
import com.mediaplayer.player.VLCAdapter
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {
    @Singleton
    @Provides
    fun provideSongsRepository(application: Application): SongsRepository {
        return SongsRepository(application)
    }
}

@Module
abstract class AbstractApplicationModule {
    @Singleton
    @Binds
    abstract fun playerAdapter(playerAdapter: VLCAdapter): PlayerAdapter
}