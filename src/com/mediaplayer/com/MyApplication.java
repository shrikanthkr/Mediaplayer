package com.mediaplayer.com;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by shrikanth on 10/17/15.
 */
public class MyApplication extends Application {

    static Context context;

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
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
