package com.mediaplayer.app.di.modules

import com.mediaplayer.player.PlayerAdapter
import com.mediaplayer.player.VLCAdapter
import com.mediaplayer.ui.notifications.NotificationService
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
class ApplicationModule

@Module
abstract class AbstractApplicationModule {
    @Singleton
    @Binds
    abstract fun playerAdapter(playerAdapter: VLCAdapter): PlayerAdapter

    @Binds
    abstract fun service(notificationService: NotificationService): NotificationService
}