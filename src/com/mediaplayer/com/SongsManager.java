package com.mediaplayer.com;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.utility.SongsHolder;

public class SongsManager {
	static SongsManager  manager;
	private  Context context;
	SongsHolder holder;
	Music music;
	SongInfoDatabase database;
	SongsManager.SongsListeners listener;
    boolean isRepeat = false;
    boolean isShuffle = false;

	public boolean isPausedFfromHeadSet() {
		return pausedFfromHeadSet;
	}

	public void setPausedFfromHeadSet(boolean pausedFfromHeadSet) {
		this.pausedFfromHeadSet = pausedFfromHeadSet;
	}

	boolean pausedFfromHeadSet = false;
    public boolean isShuffle() {
        return isShuffle;
    }

    public void setIsShuffle(boolean isShuffle) {
        this.isShuffle = isShuffle;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setIsRepeat(boolean isRepeat) {
        this.isRepeat = isRepeat;
    }

    public static SongsManager getInstance(){
		if(manager==null){
			manager = new SongsManager();
			manager.holder = new SongsHolder();
			manager.music = new Music(MyApplication.getContext(),manager.completionListener);
		}
		return manager;
	}
	public void setListener(SongsListeners listener){
		this.listener = listener;
	}

	public void pause(){
		music.pause();
	}

	public void play(){
		SongInfo currentSongInfo  = holder.getCurrentSongInfo();
		FileDescriptor fd = getFileDescriptor(currentSongInfo);
		if(music!=null) music.reset();
		try{
			music.setFileDescriptor(fd);
		}catch(RuntimeException e){
			return;
		}

		music.play();
		if(listener!=null) listener.onSongStarted(currentSongInfo);
	}
	public void resume(){
        if(holder.getSongQueue().size() <= 1 && getCurrentSongInfo()==null){
            play();
        }
		music.resume();
	}
	public void playSelectedSong(SongInfo info){
		if( holder.getSongQueue().indexOf(info) == -1){
			holder.addSongToQueue(info);
		}
		holder.setCurrentSongInfo(info);
		if(listener!=null) listener.onSongChanged(info);
	}
	public void playNextSong() {
		int currentSongIndex = holder.getSongQueue().indexOf(holder.getCurrentSongInfo());
		SongInfo nextSong;
        if(currentSongIndex < holder.getSongQueue().size() - 1){
            nextSong =holder.getSongQueue().get(currentSongIndex + 1);
        }else{
            if(isRepeat()){
                nextSong =holder.getSongQueue().get(0);
            }else{
                database = SongInfoDatabase.getInstance();
                nextSong = database.getNextSong(holder.getCurrentSongInfo());
                holder.addSongToQueue(nextSong);
            }
        }
		holder.setCurrentSongInfo(nextSong);
		play();
		if(listener!=null) listener.onSongChanged(nextSong);
	}
	public void playPreviousSong(){
		int currentSongIndex = holder.getSongQueue().indexOf(holder.getCurrentSongInfo());
		SongInfo prevSong;
		if (currentSongIndex > 0) {
			prevSong = holder.getSongQueue().get(currentSongIndex - 1);
		} else {

			prevSong = holder.getSongQueue().getLast();
		}
		holder.setCurrentSongInfo(prevSong);
		play();
		if(listener!=null) listener.onSongChanged(prevSong);
	}

    public void shuffleSongs(){
        Collections.shuffle(holder.getSongQueue());
    }

	public SongInfo getCurrentSongInfo(){
		return holder.getCurrentSongInfo();
	}
	public int getSongCurrentPosition(){
		return music.getCurrentPosition();
	}

	private FileDescriptor getFileDescriptor(SongInfo songInfo){
		FileInputStream fis = null;
		FileDescriptor fileDescriptor = null;
		try {
			fis = new FileInputStream(new File(songInfo.getData()));
			 fileDescriptor = fis.getFD();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	return fileDescriptor;
	}


	public LinkedList<SongInfo> getSongsList(){
		return holder.getSongQueue();
	}

	public void seekPlayerTo(int seektime) {
		music.seekTo(seektime * 1000);
	}

	public void appendSongs(LinkedList<SongInfo> songList) {
		holder.getSongQueue().addAll(songList);
	}

	public void addSelectedSongs(LinkedList<SongInfo> songList) {
		holder.getSongQueue().clear();
		holder.getSongQueue().addAll(songList);
	}
	public void addSong(SongInfo info){
		holder.addSongToQueue(info);
		if(listener!=null) {
			listener.onSongAdded(info);
		}
	}

    MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            int duration = mediaPlayer.getDuration()/1000;
            int current = mediaPlayer.getCurrentPosition()/1000;

            if(listener!=null && current > 10) {
                listener.onSongCompleted();
            }

        }
    };

	public boolean isPlaying() {
		return music.isPlaying();
	}

	public void destroy(){
		music.dispose();
		holder = null;
		music = null;
		manager = null;
	}
	public interface SongsListeners{
		void onSongStarted(SongInfo songInfo);
		void onSongChanged(SongInfo songInfo);
		void onSongAdded(SongInfo songInfo);
        void onSongCompleted();
	}
}
