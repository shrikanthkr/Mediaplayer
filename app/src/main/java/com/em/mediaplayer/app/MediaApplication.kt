package com.em.mediaplayer.app

import android.annotation.SuppressLint
import android.app.Application
import com.em.mediaplayer.app.di.components.AppComponent
import com.em.mediaplayer.app.di.components.DaggerAppComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope

@ExperimentalStdlibApi
@SuppressLint("Registered")
@FlowPreview
@ExperimentalCoroutinesApi
class MediaApplication : Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .application(this)
                .ioDispatcher(Dispatchers.IO)
                .globalScope(GlobalScope)
                .build()
    }
}