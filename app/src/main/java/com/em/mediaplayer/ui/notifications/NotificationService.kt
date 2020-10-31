package com.em.mediaplayer.ui.notifications

import android.app.ActivityManager
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.em.mediaplayer.app.MediaApplication
import com.em.mediaplayer.player.PlayerController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview




class NotificationService : Service() {

    private lateinit var controller: PlayerController
    private lateinit var notificationManager: MediaNotificationManager
    private lateinit var activityManager: ActivityManager

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service Created ${this.hashCode()}")
        activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val androidNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        controller = (this.application as MediaApplication).appComponent.controller()
        notificationManager = MediaNotificationManager(this, controller, startId, androidNotificationManager)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service Started ${this.hashCode()}")
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        if (activityManager.appTasks.size == 0) {
            stopSelfResult(startId)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        Log.d(TAG, "Service Destroyed ${this.hashCode()}")
        super.onDestroy()
    }


    companion object {
        private const val TAG = "NotificationService"
        private const val startId = 1
    }

}