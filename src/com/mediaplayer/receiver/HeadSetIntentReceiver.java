package com.mediaplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.ScaleGestureDetector;

import com.mediaplayer.com.SongsManager;
import com.mediaplayer.manager.BroadcastManager;

public class HeadSetIntentReceiver extends BroadcastReceiver {

	String TAG = "HEAD SET RECEIVER";

	@Override
	public void onReceive(Context ctx, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
			int state = intent.getIntExtra("state", -1);
			switch (state) {
			case 0:
				Log.d(TAG, "Headset is unplugged");
				if(SongsManager.getInstance().isPlaying()){
					ctx.sendBroadcast(new Intent(BroadcastManager.NOTIFICATION_PAUSE));
					SongsManager.getInstance().setPausedFfromHeadSet(true);
				}
				break;
			case 1:
				Log.d(TAG, "Headset is plugged");
				if(SongsManager.getInstance().isPausedFfromHeadSet()){
					ctx.sendBroadcast(new Intent(BroadcastManager.NOTIFICATION_RESUME));
					SongsManager.getInstance().setPausedFfromHeadSet(false);
				}
				break;
			default:
				//Log.d(TAG, "I have no idea what the headset state is");
			}
			Intent headset = new Intent(BroadcastManager.HEAD_SET_STATE_UPDATE);
			Bundle b= new Bundle();
			headset.putExtras(b);
			LocalBroadcastManager.getInstance(ctx).sendBroadcast(headset);
		}

	}
}