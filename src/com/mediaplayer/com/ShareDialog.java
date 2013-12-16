package com.mediaplayer.com;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mediaplayer.listener.ProgressUpdateIdentifyThread;
import com.mediaplayer.listener.UpdateNowPlayingUIListener;
import com.mediaplayer.utility.StaticMusic;

public class ShareDialog implements ProgressUpdateIdentifyThread {

	ImageButton fb_share, identify, share;
	ShareDialog listener;
	Activity context;
	static IdentifyActivityThread identifyActivityThread;
	int progress = 0;
	ProgressBar pb;
	private final Handler mTwitterHandler = new Handler();
	public boolean identify_in_progress=false;
	UpdateNowPlayingUIListener nowplayingUIListener;



	/**
	 * @return the identify_in_progress
	 */
	public boolean isIdentify_in_progress() {
		return identify_in_progress;
	}

	/**
	 * @param identify_in_progress the identify_in_progress to set
	 */
	public void setIdentify_in_progress(boolean identify_in_progress) {
		this.identify_in_progress = identify_in_progress;
	}

	final Runnable updateProgrssBar = new Runnable() {
		public void run() {
			if (progress < 100)
				pb.setProgress(progress);
			else{
				pb.setVisibility(View.INVISIBLE);
				identify_in_progress=false;
			}
		}
	};
	private UpdateListener updateListener = new UpdateListener() {

		@Override
		public void onUpdate(boolean changed) {
			// TODO Auto-generated method stub
			//Log.i("DIALOG", "false set");

			identify_in_progress=false;
			if (changed) {

				context.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated
						// method stub
						nowplayingUIListener.updateNWUIListener();
						Toast.makeText(
								context,
								"Updated",
								Toast.LENGTH_LONG)
								.show();

					}
				});

			} else {
				context.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated
						// method stub
						new InfoDialog(context,
								"Sorry we were unable to find your request. ")
						.show();
						;
					}
				});

			}

		}
	};
	ProgressUpdateIdentifyThread progressListener;
	final Runnable mUpdateTwitterNotification = new Runnable() {
		public void run() {
			Toast.makeText(context, "Tweet sent !", Toast.LENGTH_LONG).show();
		}
	};

	final Runnable mUpdateIdentifyNotification_false = new Runnable() {
		public void run() {
			Toast.makeText(context, "A Process in already identifying", Toast.LENGTH_LONG).show();
		}
	};
	final Runnable mUpdateTwitterNotification_failed = new Runnable() {
		public void run() {
			Toast.makeText(context, "Not Tweeted", Toast.LENGTH_LONG).show();
		}
	};

	public ShareDialog(Activity context,ProgressBar pb,UpdateNowPlayingUIListener nowplayingUIListener ) {
		listener = this;
		this.context = context;
		progressListener = this;
		this.pb=pb;
		this.nowplayingUIListener=nowplayingUIListener;
		progress=0;
		// TODO Auto-generated constructor stub
	}



	
	public void findSong() {
		// TODO Auto-generated method stub

		
		
			try {
				identifyActivityThread.file.delete();

			} catch (NullPointerException nullp) {
			}
			pb.setVisibility(View.VISIBLE);
			pb.setProgress(0);
			identifyActivityThread = new IdentifyActivityThread(
					StaticMusic.songInfo.getData().toString(), context,
					updateListener, progressListener);
			if(!identify_in_progress){
				identifyActivityThread .start();
				identify_in_progress=true;
			}else{
				mTwitterHandler.post(mUpdateIdentifyNotification_false);
			}
			

	}


	public interface UpdateListener {
		public void onUpdate(boolean changed);
	}

	@Override
	public void onProgressChangeUpdate(int progress) {
		// TODO Auto-generated method stub
		this.progress = progress;
		mTwitterHandler.post(updateProgrssBar);

	}

	/* (non-Javadoc)
	 * @see android.app.Dialog#onBackPressed()
	 */

}
