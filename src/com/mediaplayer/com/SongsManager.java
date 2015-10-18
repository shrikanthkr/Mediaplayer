package com.mediaplayer.com;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.fragments.NowPlayingFragment;
import com.mediaplayer.utility.SongsHolder;

public class SongsManager {
	static SongsManager  manager;
	private  Activity context;
	SongsHolder holder;
	Music music;
	SongInfoDatabase database;
	SongsManager.SongsListeners listener;
    boolean isRepeat = false;
    boolean isShuffle = false;
    Map<String, String> shuffleMap = new HashMap<>();

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
		music.setFileDescriptor(fd);
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
		if( holder.getSongQueue().indexOf(info) == -1)
			holder.addSongToQueue(info);
		play(info);
		if(listener!=null) listener.onSongChanged(info);
	}
	public void play(SongInfo info){
		holder.setCurrentSongInfo(info);
		play();
	}

	public void playNextSong() {
		int currentSongIndex = holder.getSongQueue().indexOf(holder.getCurrentSongInfo());
		SongInfo nextSong;
        if(currentSongIndex < holder.getSongQueue().size() - 1){
            nextSong =holder.getSongQueue().get(currentSongIndex + 1);
        }else{
            if(isRepeat()){
                nextSong =holder.getSongQueue().get(0);
            }else if(isShuffle()){
                nextSong =holder.getSongQueue().get(0);
            }else{
                database = SongInfoDatabase.getInstance();
                nextSong = database.getNextSong(holder.getCurrentSongInfo());
                holder.addSongToQueue(nextSong);
            }
        }
		play(nextSong);
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
		play(prevSong);

		if(listener!=null) listener.onSongChanged(prevSong);
	}
	public void setContext(Activity context) {
		this.context = context;
		if(holder==null){
			holder = new SongsHolder();
		}
		if(music == null){
			music = new Music(context,completionListener);
		}
	}

	public SongInfo getCurrentSongInfo(){
		return holder.getCurrentSongInfo();
	}
	public int getSongCurrentPosition(){
		return music.getCurrentPosition();
	}

	private void randomSong(){

	}

	private void repeatSong(){

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

            if(listener!=null && duration - 10 <= current) {
                listener.onSongCompleted();
            }

        }
    };

	public boolean isPlaying() {
		return music.isPlaying();
	}

	public interface SongsListeners{
		void onSongStarted(SongInfo songInfo);
		void onSongChanged(SongInfo songInfo);
		void onSongAdded(SongInfo songInfo);
        void onSongCompleted();
	}
}
