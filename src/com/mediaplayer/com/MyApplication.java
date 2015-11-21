package com.mediaplayer.com;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import com.crashlytics.android.Crashlytics;
import com.mediaplayer.receiver.HeadSetIntentReceiver;

import io.fabric.sdk.android.Fabric;

/**
 * Created by shrikanth on 10/17/15.
 */
public class MyApplication extends Application {

    static Context context;
    HeadSetIntentReceiver headSetReceiver;

    public static Context getContext(){
        return context;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        context = this;
        SongsManager.getInstance().setContext(this);
        startService(new Intent(context, NotificationService.class));
        registerReceivers();
    }

    private void registerReceivers() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        headSetReceiver =  new HeadSetIntentReceiver();
        registerReceiver(headSetReceiver, filter);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unRegisterListeners();
    }

    private void unRegisterListeners() {
        unregisterReceiver(headSetReceiver);
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;
}
