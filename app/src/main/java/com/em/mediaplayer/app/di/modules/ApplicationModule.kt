package com.em.mediaplayer.app.di.modules

import com.em.mediaplayer.player.PlayerAdapter
import com.em.mediaplayer.player.VLCAdapter
import com.em.mediaplayer.ui.notifications.NotificationService
import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@Module
class ApplicationModule

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