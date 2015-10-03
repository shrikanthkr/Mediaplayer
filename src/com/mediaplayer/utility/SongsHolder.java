package com.mediaplayer.utility;

import java.util.LinkedList;

import com.mediaplayer.com.Music;
import com.mediaplayer.com.SongInfo;

public class SongsHolder {

	public  LinkedList<SongInfo > songQueue;
	public  SongInfo currentSongInfo;
	public static int smoothScrollTo=0;

	public SongInfo getCurrentSongInfo() {
		return currentSongInfo;
	}

	public void setCurrentSongInfo(SongInfo currentSongInfo) {
		this.currentSongInfo = currentSongInfo;
	}

	public  SongInfo getCurrentSongInfoSongInfo() {
		return currentSongInfo;
	}

	public void setSongInfo(SongInfo songInfo) {
		songInfo = songInfo;
	}

	public LinkedList<SongInfo> getSongQueue() {
		return songQueue;
	}

	public  void setSongQueue(LinkedList<SongInfo> songQueue) {
		this.songQueue = songQueue;
	}
	public  void addSongToQueue(SongInfo songInfo){
		if(songQueue == null){
			songQueue = new LinkedList<>();
		}
		songQueue.addLast(songInfo);
	}

}
