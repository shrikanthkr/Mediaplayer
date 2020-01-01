package com.mediaplayer.app.di.modules

import android.app.Application
import com.mediaplayer.db.SongsRepository
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