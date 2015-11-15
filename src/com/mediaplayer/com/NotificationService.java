package com.mediaplayer.com;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.mediaplayer.manager.BroadcastManager;

/**
 * Implementation of App Widget functionality.
 */
public class NotificationService extends Service {


    @Override
    public void onCreate() {
       registerReceiver(notificationReceiver, new IntentFilter((BroadcastManager.NOTIFICATION_HANDLER)));
        super.onCreate();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(BroadcastManager.NOTIFICATION_HANDLER)){
                Bundle b = intent.getExtras();
                String info = b.getString(BroadcastManager.NOTIFICATION_HANDLER);
                switch (info){
                    case BroadcastManager.NOTIFICATION_PAUSE:
                        break;
                    case BroadcastManager.NOTIFICATION_PLAY:
                        break;
                    case BroadcastManager.NOTIFICATION_NEXT:
                        break;
                    case BroadcastManager.NOTIFICATION_PREV:
                        break;
                }
            }
        }
    };
}

