
package com.mediaplayer.com;
public class Nowplaying{

}
/*
package com.mediaplayer.com;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devsmart.android.ui.HorizontalListView;
import com.mediaplayer.adapter.NowPlayingHorizontalAdapter;
import com.mediaplayer.listener.UpdateNowPlayingUIListener;
import com.mediaplayer.utility.SongsHolder;
import com.mediaplayer.utility.Util;

public class Nowplaying extends Activity implements OnGlobalLayoutListener,
OnTouchListener, OnClickListener, OnItemClickListener,UpdateNowPlayingUIListener{

	ImageView playbutton_imageview;
	Button slide_nowplaying_button;
	ImageButton equalizer_imageButton, more_imagebutton;
	ImageButton previous, next, shuffle, repeat, edit_nowplaying,
	delete_nowplaying;
	int[] image_view_params = new int[2];
	float pos_x, pos_y;
	SeekBar seekbar;
	LinearLayout seekbar_layout;
	LinearLayout seekbar_layout_grey_bg;
	Timer timer;
	TimerTask timer_task;
	boolean isPaused;
	LinearLayout mainLayout;
	ViewTreeObserver vto;
	int time = 0;
	int duration = 0;
	Util util;
	//Music music;
	FileInputStream fis;
	Bitmap seek_image;
	FileDescriptor fileDescriptor;
	SongInfo songInfo;
	RelativeLayout r;
	HorizontalListView nowplaying_horizontal;
	NowPlayingHorizontalAdapter horizontal_adapter;
	boolean isSeekable_position = false;
	boolean isSongChanged = false;
	Activity activity;
	ArrayList<SongInfo> horizontal_songInfo_array;
	ListEditorDialog listeditor_dialog;
	TextView artist_header, song_header, duration_header,
	tempduration_textview;
	DecimalFormat format;
	View popupView;
	OnClickListener clickListener;
	LinearLayout fb_share, identify, share;
	boolean popupwindow_flag = false;
	boolean identify_running_flag;
	boolean slidebutton_clicked;
	PopupWindow popupWindow;
	ImageView measure_view;
	IdentifyActivityThread identifyActivityThread;
	Context context;
	ProgressBar pb;

	ShareDialog shareDialog;




	private void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.nowplaying_xml);
		activity = this;
		r = (RelativeLayout) findViewById(R.id.play_button_rlayout);
		mainLayout = (LinearLayout) findViewById(R.id.nowplaying_id);
		slide_nowplaying_button = (Button) findViewById(R.id.slide_nowplaying_button);
		playbutton_imageview = (ImageView) findViewById(R.id.playbutton_imageView);
		seekbar_layout = (LinearLayout) findViewById(R.id.seekbar_layout);
		seekbar_layout_grey_bg = (LinearLayout) findViewById(R.id.seekbar_layout_grey_bg);
		previous = (ImageButton) findViewById(R.id.previous_button);
		next = (ImageButton) findViewById(R.id.nextbutton);
		equalizer_imageButton = (ImageButton) findViewById(R.id.equalizer_imageButton);
		equalizer_imageButton.setOnClickListener(this);
		mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
		shuffle = (ImageButton) findViewById(R.id.shuffle_button);
		repeat = (ImageButton) findViewById(R.id.repeat_button);
		edit_nowplaying = (ImageButton) findViewById(R.id.edit_nowplaying);
		delete_nowplaying = (ImageButton) findViewById(R.id.delete_nowplaying);
		edit_nowplaying.setOnClickListener(this);
		delete_nowplaying.setOnClickListener(this);
		repeat.setOnClickListener(this);
		shuffle.setOnClickListener(this);
		setShuffleRepeatButtons();
		artist_header = (TextView) findViewById(R.id.artist_now_playingheader);
		song_header = (TextView) findViewById(R.id.song_now_playingheader);
		song_header.setSelected(true);
		duration_header = (TextView) findViewById(R.id.duration_now_playingheader);
		tempduration_textview = (TextView) findViewById(R.id.tempduration_textView);
		tempduration_textview.setVisibility(View.INVISIBLE);
		more_imagebutton = (ImageButton) findViewById(R.id.more_imageButton);
		measure_view = (ImageView) findViewById(R.id.seek_measure_imageView);
		pb = (ProgressBar) findViewById(R.id.identify_progressbar);
		pb.setVisibility(View.INVISIBLE);
		more_imagebutton.setOnClickListener(this);
		clickListener = this;
		slidebutton_clicked=false;

		nowplaying_horizontal = (HorizontalListView) findViewById(R.id.nowplaying_horizontal);
		updateNowPlayingListUI();
		vto = mainLayout.getViewTreeObserver();
		util = new Util();
		songInfo = new SongInfo();
		shareDialog=new ShareDialog(activity,pb,this);

		try {
			seekbar_layout.setOnTouchListener(this);
			previous.setOnClickListener(this);
			next.setOnClickListener(this);
			playbutton_imageview.setOnClickListener(this);
			playSong();
			setEqualizer();
			SongsHolder.music.mediaPlayer
			.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					SongsHolder.music.playNextSong();
					refresh();
				}
			});

			timer.cancel();
			timer_task.cancel();
		} catch (Exception e) {
			r.setClickable(false);

		}

	}

	private void setShuffleRepeatButtons() {
		// TODO Auto-generated method stub
		if (Music.isShuffle) {
			shuffle.setBackgroundResource(R.drawable.shuffle);
			repeat.setBackgroundResource(R.drawable.repeat_off);

		} else if (Music.isRepeat) {
			repeat.setBackgroundResource(R.drawable.repeat);
			shuffle.setBackgroundResource(R.drawable.shuffle_off);
		} else {
			repeat.setBackgroundResource(R.drawable.repeat_off);
			shuffle.setBackgroundResource(R.drawable.shuffle_off);
		}

	}






	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		// TODO Auto-generated method stub
		float x1 = arg1.getX();
		float y1 = arg1.getY();
		try {
			popupWindow.dismiss();
			popupwindow_flag = false;
			more_imagebutton.setOnClickListener(clickListener);
		} catch (Exception e) {
		}
		try {
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				isSeekable_position = positionSeekable(x1, y1);

			} else if (arg1.getAction() == MotionEvent.ACTION_MOVE
					&& isSeekable_position) {
				tempduration_textview.setVisibility(View.VISIBLE);
				seekbar.calculateTempSeek(x1, y1);
				int temp = seekbar.getSeekedTime(duration);
				tempduration_textview.setText(temp / 60 + ":" + temp % 60);
			} else {

				seekbar.setSeeking(false);
				if (isSeekable_position) {
					time = seekbar.getSeekedTime(duration);
					SongsHolder.music.seekTo(time * 1000);
					seekbar.callfromTimerTask(time, duration);
					isSeekable_position = false;
					tempduration_textview.setVisibility(View.INVISIBLE);
				}
			}
		} catch (NullPointerException e) {
		}
		return true;
	}

	private boolean positionSeekable(float x1, float y1) {
		// TODO Auto-generated method stub
		float allowed_position = (float) Math.sqrt(Math.pow(
				(x1 - seekbar.getCenter_x()), 2)
				+ Math.pow((y1 - seekbar.getCenter_y()), 2));
		if (allowed_position < seekbar.getRadius() + 40) {
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		try {
			try {
				popupWindow.dismiss();
				popupwindow_flag = false;
				more_imagebutton.setOnClickListener(clickListener);
			} catch (Exception e) {
			}

			switch (arg0.getId()) {

			case R.id.nextbutton:
				isSongChanged = true;
				SongsHolder.music.playNextSong();
				refresh();
				break;
			case R.id.previous_button:
				isSongChanged = true;
				try {
					SongsHolder.music.playPreviousSong();
					refresh();
				} catch (NoSuchElementException e) {
				}

				break;
			case R.id.playbutton_imageView:
				try {
					if (SongsHolder.music.isPlaying()) {

						SongsHolder.music.pause();
						setPlayButton();

					} else {

						SongsHolder.music.play();
						setPauseButton();
					}
				} catch (Exception e) {
				}
				break;
			case R.id.equalizer_imageButton:

				startActivity(new Intent(this, AudioFxDemo.class));
				AudioFxDemo.mEqualizer.release();

				break;
			case R.id.shuffle_button:
				if (Music.isShuffle) {
					Music.isShuffle = false;
					shuffle.setBackgroundResource(R.drawable.shuffle_off);
				} else {
					Music.isShuffle = true;
					Music.isRepeat = false;
					shuffle.setBackgroundResource(R.drawable.shuffle);
					repeat.setBackgroundResource(R.drawable.repeat_off);
				}
				break;
			case R.id.repeat_button:
				if (Music.isRepeat) {
					Music.isRepeat = false;
					repeat.setBackgroundResource(R.drawable.repeat_off);
				} else {
					Music.isRepeat = true;
					Music.isShuffle = false;
					repeat.setBackgroundResource(R.drawable.repeat);
					shuffle.setBackgroundResource(R.drawable.shuffle_off);
				}
				break;
			case R.id.delete_nowplaying:
				SongsHolder.songQueue.clear();
				SongsHolder.music.mediaPlayer.stop();
				SongsHolder.music.mediaPlayer.release();
				SongsHolder.music = null;
				SongsHolder.songInfo=null;
				refresh();
				break;
			case R.id.edit_nowplaying:

				if (SongsHolder.songQueue.size() <= 0) {
					break;
				}
				final ArrayList<SongInfo> songs_array = new ArrayList<SongInfo>(
						SongsHolder.songQueue);
				//Log.i("NOW PLAYING", "QUEUE SONG SIZE" + songs_array.size());
				listeditor_dialog = new ListEditorDialog(this, songs_array);

				listeditor_dialog.show();
				ImageButton reset_playaueue = (ImageButton) listeditor_dialog
						.findViewById(R.id.reset_playaueue);
				reset_playaueue.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ArrayList<SongInfo> temp_array = new ArrayList<SongInfo>();
						ArrayList<Integer> cursor_positions = listeditor_dialog
								.getAdapter().getCursorPositions();
						for (int x = 0; x < cursor_positions.size(); x++) {
							temp_array.add(songs_array.get(cursor_positions
									.get(x)));

						}
						SongsHolder.songQueue.clear();
						if (temp_array.size() > 0) {
							SongsHolder.songQueue.addAll(temp_array);
							updateNowPlayingListUI();
							listeditor_dialog.dismiss();
						} else {
							SongsHolder.songQueue.clear();
							SongsHolder.music.pause();
							SongsHolder.setMusic(null);
							SongsHolder.setSongInfo(null);
							SongsHolder.music.mediaPlayer.stop();
							SongsHolder.music.mediaPlayer.release();
							SongsHolder.music = SongsHolder.getMusic();
							refresh();
							listeditor_dialog.dismiss();
						}

					}
				});
				break;
			case R.id.more_imageButton:
				Toast.makeText(this, "Searching for the song", Toast.LENGTH_LONG).show();
				shareDialog.findSong();
			}
		} catch (NullPointerException e) {
		}
	}


	public void refresh() {

		Intent toNowPlaying = getIntent();
		toNowPlaying.putExtra("activity", "songlist");
		//Log.i("NOW PLAYIN", "REFRESH");
		init();

	}

	public void setPlayButton() {
		isSongChanged = false;
		playbutton_imageview.setBackgroundResource(R.drawable.play);
	}

	public void setPauseButton() {
		isSongChanged = false;
		playbutton_imageview.setBackgroundResource(R.drawable.pause);
	}
	
	private void setEqualizer(){
	*/
/*	try{
			//Log.i("EQUALIZER", ""+AudioFxDemo.mEqualizer.hasControl());
			AudioFxDemo.mEqualizer.usePreset(StaticMusic.band_equi);
			//Log.i("EQUALIZER", ""+AudioFxDemo.mEqualizer.getCurrentPreset());
			//Log.i("EQUALIZER", ""+StaticMusic.band_equi);
		}catch(Exception e){

			e.printStackTrace();
			AudioFxDemo.getEqualizer();
			AudioFxDemo.mEqualizer.usePreset(StaticMusic.band_equi);
		}*//*

		AudioFxDemo.getEqualizer();
		AudioFxDemo.mEqualizer.usePreset((short) SongsHolder.band_equi);
		AudioFxDemo.mEqualizer.setEnabled(true);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		SongsHolder.setSongInfo(horizontal_songInfo_array.get(arg2));
		refresh();

	}

	@Override
	public void updateNWUIListener() {
		// TODO Auto-generated method stub
		updateNowPlayingListUI();
		song_header.setText(SongsHolder.songInfo.getTitle());
		artist_header.setText(SongsHolder.songInfo.getArtist());
	}



}
*/
