package com.mediaplayer.manager;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.mediaplayer.com.ContainerActivity;
import com.mediaplayer.com.R;
import com.mediaplayer.fragments.NowPlayingFragment;

/**
 * Created by shrikanth on 11/15/15.
 */
public class NotificationHelper {
    private Context parent;
    private NotificationManager nManager;
    private NotificationCompat.Builder nBuilder;
    private RemoteViews remoteView;
    public NotificationHelper(Context parent) {
        this.parent = parent;
        nBuilder = new NotificationCompat.Builder(parent)
                .setContentTitle("Parking Meter")
                .setSmallIcon(R.drawable.ic_launcher)
                .setOngoing(true);

        remoteView = new RemoteViews(parent.getPackageName(), R.layout.notificationview);

        //set the button listeners
        setupView(remoteView);
        setListeners(remoteView);
        nBuilder.setContent(remoteView);

        nManager = (NotificationManager) parent.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(2, nBuilder.build());
    }


    public void setListeners(RemoteViews view){
        //listener 1
        Intent prev = new Intent();
        prev.setAction(BroadcastManager.NOTIFICATION_HANDLER);
        prev.putExtra(BroadcastManager.NOTIFICATION_HANDLER, BroadcastManager.NOTIFICATION_PREV);
        PendingIntent previous = PendingIntent.getBroadcast(parent, BroadcastManager.NOTIFICATION_REQUEST_CODE, prev, PendingIntent.FLAG_CANCEL_CURRENT);

        view.setOnClickPendingIntent(R.id.previous_button, previous);

        //listener 2
        Intent next = new Intent(parent, ContainerActivity.class);
        next.putExtra("DO", "stop");
        PendingIntent btn2 = PendingIntent.getActivity(parent, 1, next, 0);
        view.setOnClickPendingIntent(R.id.nextbutton, btn2);
    }

    public void notificationCancel() {
        nManager.cancel(2);
    }

    public void setupView(RemoteViews view) {
    }
}
