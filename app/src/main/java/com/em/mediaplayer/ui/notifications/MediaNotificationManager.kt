package com.em.mediaplayer.ui.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.em.mediaplayer.app.R
import com.em.mediaplayer.app.activities.home.HomeActivity
import com.em.mediaplayer.app.models.PlayerState
import com.em.mediaplayer.player.PlayerController
import com.em.repository.Song
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalStdlibApi
class MediaNotificationManager(private val context: Context, private val controller: PlayerController, private val id: Int, notificationManager: NotificationManager) : BroadcastReceiver() {


    private lateinit var builder: Notification.Builder
    private lateinit var remoteView: RemoteViews
    var notification: Notification? = null
    val instance = LocalBroadcastManager.getInstance(context)
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    private val currentSongObserver = Observer<Song> {
        remoteView.setTextViewText(R.id.title, it.title)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private val playerStateObserver = Observer<PlayerState> {
        when (it) {
            is PlayerState.Paused -> {
                remoteView.setImageViewIcon(R.id.play_pause, Icon.createWithResource(context, R.drawable.ic_play_arrow))
                setupViews(remoteView)
            }
            is PlayerState.Started -> {
                remoteView.setImageViewIcon(R.id.play_pause, Icon.createWithResource(context, R.drawable.ic_pause))
                setupViews(remoteView)
            }

        }

    }

    init {
        prepareNotification()
    }

    private fun prepareNotification() {
        val pendingIntent: PendingIntent =
                Intent(context, HomeActivity::class.java).let { notificationIntent ->
                    PendingIntent.getActivity(context, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                }
        remoteView = RemoteViews(context.applicationContext.applicationInfo.packageName, R.layout.notification_view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(context.getString(R.string.channel_id), context.getString(R.string.channel_name), NotificationManager.IMPORTANCE_DEFAULT)
            channel.setSound(null, null)
            channel.setShowBadge(false)
            channel.importance = NotificationManager.IMPORTANCE_LOW
            notificationManager.createNotificationChannel(channel)
            builder = Notification.Builder(context, channel.id)
                    .setContentTitle(context.getText(R.string.play_icon_description))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))
                    .setCustomContentView(remoteView)
                    .setContentIntent(pendingIntent)
                    .setTicker(context.getText(R.string.app_name))
            builder.build()
        } else {
            @Suppress("DEPRECATION")
            builder = Notification.Builder(context)
                    .setContentTitle(context.getText(R.string.search_title))
                    .setContentText(context.getText(R.string.app_name))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setTicker(context.getText(R.string.app_name))

        }

    }


    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            BroadCastConstants.NOTIFICATION_PLAY -> {
                togglePlay()
            }
        }
    }

    private fun togglePlay() = Unit


    private fun setupViews(remoteView: RemoteViews) {
        //listener 1
        val playIntent = Intent(BroadCastConstants.NOTIFICATION_PLAY)
        val previous = PendingIntent.getBroadcast(context, BroadCastConstants.NOTIFICATION_REQUEST_CODE, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        remoteView.setPendingIntentTemplate(R.id.play_pause, previous)
        notificationManager.notify(id, builder.build())
    }

}