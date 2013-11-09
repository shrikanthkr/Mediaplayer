package com.mediaplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

public class MusicIntentReceiver extends BroadcastReceiver {

	String TAG = "HEAD SET RECEIVER";

	@Override
	public void onReceive(Context ctx, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
			int state = intent.getIntExtra("state", -1);
			switch (state) {
			case 0:
				//Log.d(TAG, "Headset is unplugged");
				break;
			case 1:
				//Log.d(TAG, "Headset is plugged");
				break;
			default:
				//Log.d(TAG, "I have no idea what the headset state is");
			}
		}

	}
}