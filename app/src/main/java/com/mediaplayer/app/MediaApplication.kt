package com.mediaplayer.app

import android.annotation.SuppressLint
import android.app.Application
import com.mediaplayer.app.di.components.AppComponent
import com.mediaplayer.app.di.components.DaggerAppComponent
import kotlinx.coroutines.Dispatchers

@SuppressLint("Registered")
class MediaApplication : Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .application(this)
                .ioDispatcher(Dispatchers.IO)
                .build()
    }
}