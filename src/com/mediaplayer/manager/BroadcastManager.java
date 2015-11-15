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

    public static void unRegisters(BroadcastReceiver receiver){
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }


    public static final String PLAYSONG = "playsong";
    public static final String APPEND_LIST = "append_selected";
    public static final String PLAY_SELECTED = "play_selected";
    public static final String SONG_KEY = "song";
    public static final String LIST_KEY = "list";
    public static final String HEAD_SET_STATE_UPDATE = "head_set_state_update";

    public static final int NOTIFICATION_REQUEST_CODE = 9999;
    public static final String NOTIFICATION_HANDLER = "noti_handle";
    public static final String NOTIFICATION_PLAY = "noti_play";
    public static final String NOTIFICATION_PAUSE = "noti_pause";
    public static final String NOTIFICATION_PREV = "noti_prev";
    public static final String NOTIFICATION_NEXT = "noti_next";
    public static final String NOTIFICATION_RESUME = "noti_resume";
}
