package com.mediaplayer.com;

import java.io.FileDescriptor;
import java.io.IOException;

import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.fragments.NowPlayingFragment;
import com.mediaplayer.utility.SongsHolder;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;

public class Music{

	static  MediaPlayer mediaPlayer;
	MediaPlayer.OnCompletionListener completionListener;
	public Music(Activity context,MediaPlayer.OnCompletionListener completionListener)  {
		mediaPlayer = null;
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		this.completionListener =completionListener;
		mediaPlayer.setOnCompletionListener(completionListener);
	}

	public void setFileDescriptor(FileDescriptor fileDescriptor) {
		try {
			if(mediaPlayer.isPlaying()) {
				stop();
			}
			mediaPlayer.setDataSource(fileDescriptor);
			play();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Couldn't load music, uh oh!");
		}
	}

	public void play() {
		try {
			synchronized (this) {
				mediaPlayer.prepare();
				mediaPlayer.start();
			}
		} catch (IllegalStateException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void resume(){
		mediaPlayer.start();
	}
	public void reset() {
		if(mediaPlayer!=null){
			mediaPlayer.stop();
			mediaPlayer.reset();
		}
	}
	private void stop(){
		mediaPlayer.stop();
	}

	public int getDuration() {
		if(mediaPlayer!=null){
			return  mediaPlayer.getDuration();
		}
		return 0;
	}
	public int getCurrentPosition() {
		if(mediaPlayer!=null){
			return  mediaPlayer.getCurrentPosition();
		}
		return 0;
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