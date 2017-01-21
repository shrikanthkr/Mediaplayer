package com.mediaplayer.com;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;

import com.mediaplayer.receiver.HeadSetIntentReceiver;


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
        context = this;
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

}
