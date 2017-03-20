package com.mediaplayer.com;

import android.content.Context;
import android.net.Uri;

import com.mediaplayer.application.MyApplication;
import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.utility.SongsHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SongsManager {
	static SongsManager  manager;
	private static final String SERVER = "rtmp://ec2-35-154-51-58.ap-south-1.compute.amazonaws.com/myapp/";
	private  Context context;
	SongsHolder holder;
	Music music;
	SongInfoDatabase database;
	List<SongsListeners > listeners = new ArrayList<>();
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

    public static synchronized  SongsManager getInstance(){
		if(manager==null){
			manager = new SongsManager();
			manager.holder = new SongsHolder();
			manager.music = new Music(MyApplication.getContext(),manager.completionListener);
		}
		return manager;
	}
	public void addListener(SongsListeners listener){
		this.listeners.add(listener);
	}

	public void removeListener(SongsListeners listener){
		if(listeners != null) listeners.remove(listener);
	}

	public void pause(){
		music.pause();
	}

	public void startRemotePlay(String streamPath){
		Uri uri = Uri.parse(SERVER + streamPath);
		music.setStreamUri(uri);
		music.play();
	}
	public void play(){
		SongInfo currentSongInfo  = holder.getCurrentSongInfo();
		try{
			music.setStreamPath(currentSongInfo.getData());
		}catch(RuntimeException e){
			return;
		}

		music.play();
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
			notifyAdded(info);
		}
		holder.setCurrentSongInfo(info);
		play();
		notifyChanged(info);
	}
	public void playNextSong() {
		int currentSongIndex = holder.getSongQueue().indexOf(holder.getCurrentSongInfo());
		SongInfo nextSong;
        if(currentSongIndex < holder.getSongQueue().size() - 1){
            nextSong = holder.getSongQueue().get(currentSongIndex + 1);
			notifyChanged(nextSong);
        }else{
            if(isRepeat()){
                nextSong = holder.getSongQueue().get(0);
				notifyChanged(nextSong);
            }else{
                database = SongInfoDatabase.getInstance();
                nextSong = database.getNextSong(holder.getCurrentSongInfo());
                holder.addSongToQueue(nextSong);
				notifyAdded(nextSong);
            }
        }
		holder.setCurrentSongInfo(nextSong);
		play();
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
		notifyChanged(prevSong);
	}

    public void shuffleSongs(){
        Collections.shuffle(holder.getSongQueue());
    }

	public SongInfo getCurrentSongInfo(){
		return holder.getCurrentSongInfo();
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
		notifyAdded(info);
	}

	Music.MusicHelperInterface completionListener = new Music.MusicHelperInterface() {
		@Override
		public void onStarted() {
			notifyStarted(getCurrentSongInfo());
		}

		@Override
		public void onComplete(int duration) {
			playNextSong();
		}

		@Override
		public void timeChange(int duration, int current) {
			notifyTimeChange(duration, current);
		}
	};

	public boolean isPlaying() {
		return music.isPlaying();
	}

	public void playFromFirst(){
		pause();
		playSelectedSong(getSongsList().get(0));
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
		void onTimeChange(int duration, long currentTime);
	}

	private void notifyStarted(SongInfo songInfo){
		for(SongsListeners listener : listeners){
			listener.onSongStarted(songInfo);
		}
	}

	private void notifyChanged(SongInfo songInfo){
		for(SongsListeners listener : listeners){
			listener.onSongChanged(songInfo);
		}
	}
	private void notifyAdded(SongInfo songInfo){
		for(SongsListeners listener : listeners){
			listener.onSongAdded(songInfo);
		}
	}

	private void notifyTimeChange(int duration, int currentTime){
		for(SongsListeners listener : listeners){
			listener.onTimeChange(duration, currentTime);
		}
	}
}
