package com.mediaplayer.com;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mediaplayer.listener.ProgressUpdateIdentifyThread;
import com.mediaplayer.listener.UpdateNowPlayingUIListener;
import com.mediaplayer.utility.FBUtils;
import com.mediaplayer.utility.StaticMusic;
import com.mediaplayer.utility.TwitterUtils;

public class ShareDialog extends Dialog implements OnClickListener,ProgressUpdateIdentifyThread {

	ImageButton fb_share, identify, share;
	ShareDialog listener;
	Activity context;
	private SharedPreferences prefs;
	IdentifyActivityThread identifyActivityThread;
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
			Log.i("DIALOG", "false set");

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
		super(context,R.style.hidetitle);
		listener = this;
		this.context = context;
		this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
		progressListener = this;
		this.pb=pb;
		this.nowplayingUIListener=nowplayingUIListener;
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Dialog#show()
	 */
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
		super.show();
		setContentView(R.layout.sharedialog_xml);
		fb_share = (ImageButton) findViewById(R.id.fb_imageButton);
		identify = (ImageButton) findViewById(R.id.identify_imageButton);
		share = (ImageButton) findViewById(R.id.twitter_imageButton);
		fb_share.setOnClickListener(listener);
		identify.setOnClickListener(listener);
		share.setOnClickListener(listener);
		progress=0;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		switch (arg0.getId()) {

		case R.id.fb_imageButton:
			if (FBUtils.getSessionInfo() == null) {
				Log.i("NOW PLAYING", "Sartig activity");
				Intent fb_share = new Intent(context,
						LoginUsingActivityActivity.class);
				fb_share.putExtra("song_info", StaticMusic.songInfo);
				context.startActivity(fb_share);
			} else {
				if (FBUtils.getSessionInfo().isOpened()) {
					FBUtils.publishStory(FBUtils.getSessionInfo(),
							StaticMusic.songInfo, context);
				} else {
					Intent fb_share = new Intent(context,
							LoginUsingActivityActivity.class);
					fb_share.putExtra("song_info", StaticMusic.songInfo);
					context.startActivity(fb_share);
				}
			}
			dismiss();
			break;
		case R.id.identify_imageButton:
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
			dismiss();
			break;
		case R.id.twitter_imageButton:
			new AsyncTask<SharedPreferences, Object, Boolean>() {

				@Override
				protected Boolean doInBackground(SharedPreferences... params) {
					return TwitterUtils.isAuthenticated(params[0]);
				}

				@Override
				protected void onPostExecute(Boolean isAuthenticated) {
					if (isAuthenticated) {
						// Do processing after successful
						// authentication
						sendTweet();
					} else {
						// Do processing after authentication
						// failure
						Intent i = new Intent(context,
								PrepareRequestTokenActivity.class);
						i.putExtra("song-info", StaticMusic.songInfo);
						context.startActivity(i);
					}
				}
			}.execute(prefs);
			dismiss();
			break;
		}

	}

	public void sendTweet() {
		Thread t = new Thread() {
			public void run() {

				try {
					TwitterUtils
					.sendTweet(prefs, StaticMusic.songInfo, context);
					mTwitterHandler.post(mUpdateTwitterNotification);
				} catch (Exception ex) {
					mTwitterHandler.post(mUpdateTwitterNotification_failed);
					ex.printStackTrace();
				}
			}

		};
		t.start();

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
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		dismiss();
	}
	
}
