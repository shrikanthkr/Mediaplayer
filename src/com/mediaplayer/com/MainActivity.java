package com.mediaplayer.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.mediaplayer.fragments.LaunchTabsContainer;
import com.mediaplayer.manager.BroadcastManager;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

/**
 * Created by shrikanth on 1/21/17.
 */

public class MainActivity extends  BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadcastManager.setApplicationContext(this);
        startService(new Intent(MyApplication.getContext(), NotificationService.class));
        setContentView(R.layout.activity_container);
        launchTabs();
    }
    private void launchTabs(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        int count = getSupportFragmentManager().getBackStackEntryCount();
        LaunchTabsContainer tabsContainer = new LaunchTabsContainer();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, tabsContainer, count+"")
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterManagers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForCrashes();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(SongsManager.getInstance().getCurrentSongInfo()==null){
            sendBroadcast(new Intent(BroadcastManager.NOTIFICATION_CLOSE));
        }
        unregisterManagers();
    }


    private void checkForCrashes() {
        CrashManager.register(this);
    }


    private void unregisterManagers() {
        UpdateManager.unregister();
    }
}
