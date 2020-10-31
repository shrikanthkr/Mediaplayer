package com.em.mediaplayer.app

import android.annotation.SuppressLint
import android.app.Application
import com.em.mediaplayer.app.di.components.AppComponent
import com.em.mediaplayer.app.di.components.DaggerAppComponent
import com.em.mediaplayer.player.AdapterPrioritizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import javax.inject.Inject

@SuppressLint("Registered")
class MediaApplication : Application() {
    lateinit var appComponent: AppComponent

    @Inject
    lateinit var adapterPrioritizer: AdapterPrioritizer

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .application(this)
                .ioDispatcher(Dispatchers.IO)
                .globalScope(GlobalScope)
                .build()
        appComponent.inject(this)

    }
}