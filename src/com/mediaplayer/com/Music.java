package com.mediaplayer.com;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;

import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.utility.StaticMusic;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import android.widget.Toast;

public class Music {

	MediaPlayer mediaPlayer;
	boolean isPrepared = false;
	static boolean isRepeat = false;
	static boolean isShuffle = true;
	SongInfoDatabase database;
	Context context;
	private OnAudioFocusChangeListener audioListener=new OnAudioFocusChangeListener() {
		
		@Override
		public void onAudioFocusChange(int arg0) {
			// TODO Auto-generated method stub
			AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
			switch(arg0){
			case AudioManager.AUDIOFOCUS_LOSS:
				stop();
				break;
			case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
				setVolume(0.5f, 0.5f);
				break;
			case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
				pause();
				setVolume(0.5f, 0.5f);
				break;
			case AudioManager.AUDIOFOCUS_GAIN:
				play();
				setVolume(0.5f, 0.5f);
				break;
			
			}
		}
	};

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public boolean isRepeat() {
		return isRepeat;
	}

	public void setPlylist(boolean isRepeat) {
		Music.isRepeat = isRepeat;
	}

	public boolean isShuffle() {
		return isShuffle;
	}

	public void setShuffle(boolean isShuffle) {
		Music.isShuffle = isShuffle;
	}

	public Music(AssetFileDescriptor assetDescriptor) {
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(),
					assetDescriptor.getStartOffset(),
					assetDescriptor.getLength());
			mediaPlayer.prepare();
			isPrepared = true;
		} catch (Exception ex) {
			throw new RuntimeException("Couldn't load music, uh oh!");
		}
	}

	public Music(FileDescriptor fileDescriptor) {
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setDataSource(fileDescriptor);
			mediaPlayer.prepare();
			isPrepared = true;
		} catch (Exception ex) {
			throw new RuntimeException("Couldn't load music, uh oh!");
		}
	}

	public void onCompletion(MediaPlayer mediaPlayer) {
		synchronized (this) {
			isPrepared = false;
			playNextSong();
			// StaticMusic.setMusic(null);
		}
	}

	public void playNextSong() {
		// TODO Auto-generated method stub

		if (isRepeat) {
			StaticMusic.songInfo = StaticMusic.songQueue
					.get(StaticMusic.songQueue.indexOf(StaticMusic.songInfo));
			Log.i("NEXT TRACK", "REPEAT");

		} else if (isShuffle) {
			Log.i("NEXT TRACK", "SHUFFLE");
			if (StaticMusic.songQueue.indexOf(StaticMusic.songInfo) == StaticMusic.songQueue
					.size() - 1) {
				database = new SongInfoDatabase(context);
				database.open();
				StaticMusic.songInfo = database.nextShuffleSong();
				database.close();
				StaticMusic.songQueue.addLast(StaticMusic.songInfo);
			} else {
				StaticMusic.songInfo = StaticMusic.songQueue
						.get(StaticMusic.songQueue
								.indexOf(StaticMusic.songInfo) + 1);
			}

			
		} else {
			Log.i("Next Track Number","DUMMY NEXT");
			if (StaticMusic.songQueue.indexOf(StaticMusic.songInfo) < StaticMusic.songQueue
					.size() - 1) {
				Log.i("Next Track Number",
						1 + StaticMusic.songQueue.indexOf(StaticMusic.songInfo)
								+ "");
				StaticMusic.songInfo = StaticMusic.songQueue
						.get(StaticMusic.songQueue
								.indexOf(StaticMusic.songInfo) + 1);

			} else {
				database = new SongInfoDatabase(context);
				database.open();
				Log.i("Next Track Number","NEXT SONG IN DB");
				StaticMusic.songInfo=database.getNextSong(StaticMusic.songInfo);
				StaticMusic.songQueue.addLast(StaticMusic.songInfo);
				database.close();
				//StaticMusic.songInfo = StaticMusic.songQueue.getFirst();
			}

		}

	}

	public void playPreviousSong() {
		// TODO Auto-generated method stub
		Log.i("Previous Track Number",
				-1 + StaticMusic.songQueue.indexOf(StaticMusic.songInfo) + "");
		if (StaticMusic.songQueue.indexOf(StaticMusic.songInfo) > 0) {
			StaticMusic.songInfo = StaticMusic.songQueue
					.get(StaticMusic.songQueue.indexOf(StaticMusic.songInfo) - 1);
		} else {

			StaticMusic.songInfo = StaticMusic.songQueue.getLast();
		}
		if (isRepeat) {

		} else if (isShuffle) {

		} else {

		}

	}

	public void play() {
		if (mediaPlayer.isPlaying()) {
			return;
		}
		try {
			synchronized (this) {
				if (!isPrepared) {
					mediaPlayer.prepare();
				}
				mediaPlayer.start();
			}
		} catch (IllegalStateException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void stop() {
		mediaPlayer.stop();

		synchronized (this) {
			isPrepared = false;
		}
	}

	public int getCurrentPosition() {
		return mediaPlayer.getCurrentPosition();
	}

	public void switchTracks() {
		mediaPlayer.seekTo(0);
		mediaPlayer.pause();
	}

	public void pause() {
		mediaPlayer.pause();
	}

	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	public boolean isLooping() {
		return mediaPlayer.isLooping();
	}

	public void setLooping(boolean isLooping) {
		mediaPlayer.setLooping(isLooping);
	}

	public void setVolume(float volumeLeft, float volumeRight) {
		mediaPlayer.setVolume(volumeLeft, volumeRight);
	}

	public void dispose() {
		if (mediaPlayer.isPlaying()) {
			stop();
		}
		mediaPlayer.release();
	}

	public void seekTo(int position) {
		mediaPlayer.seekTo(position);
	}
}