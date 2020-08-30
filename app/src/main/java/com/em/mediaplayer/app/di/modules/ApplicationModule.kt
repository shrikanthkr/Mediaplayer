package com.em.mediaplayer.app.di.modules

import com.em.mediaplayer.app.di.qualifiers.SingleThreadDispatcher
import com.em.mediaplayer.player.PlayerAdapter
import com.em.mediaplayer.player.VLCAdapter
import com.em.mediaplayer.ui.notifications.NotificationService
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @SingleThreadDispatcher
    fun singleThreadDispatcher(): CoroutineDispatcher {
        return Executors.newFixedThreadPool(1).asCoroutineDispatcher()
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@Module
abstract class AbstractApplicationModule {
    @Singleton
    @Binds
    abstract fun playerAdapter(playerAdapter: VLCAdapter): PlayerAdapter

    @Binds
    abstract fun service(notificationService: NotificationService): NotificationService
}