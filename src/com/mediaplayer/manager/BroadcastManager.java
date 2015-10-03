package com.mediaplayer.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by shrikanth on 10/3/15.
 */
public class BroadcastManager {
    static Context context;
    public static void setApplicationContext(Context context){
        BroadcastManager.context = context;
    }

    public static void registerForEvent( String eventName, BroadcastReceiver receiver){
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver,new IntentFilter(eventName));
    }



}
