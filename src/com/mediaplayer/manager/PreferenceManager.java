package com.mediaplayer.manager;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.mediaplayer.com.MyApplication;

/**
 * Created by shrikanth on 11/21/15.
 */
public enum PreferenceManager {
    INSTANCE;
    SharedPreferences preferences = MyApplication.getContext().getSharedPreferences("media",Context.MODE_PRIVATE);
    SharedPreferences.Editor prefsEditor = preferences.edit();
    public void setIsRepeat(boolean flag){
        prefsEditor.putBoolean("is_repeat",flag).commit();
    }

    public boolean getIsRepeat(){
        return preferences.getBoolean("is_repeat",false);
    }

    public void setIsShuffle(boolean flag){
        prefsEditor.putBoolean("is_shuffle",flag).commit();
    }

    public boolean getIsShuffle(){
        return preferences.getBoolean("is_shuffle",false);
    }
}
