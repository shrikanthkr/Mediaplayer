package com.mediaplayer.utility;

import java.util.HashMap;
import java.util.LinkedList;

import android.media.audiofx.Equalizer;

import com.mediaplayer.com.Music;
import com.mediaplayer.com.SongInfo;

public class StaticMusic {
	
	public static Music music;
	public static LinkedList<SongInfo > songQueue;
	public static SongInfo songInfo;
	public static int smoothScrollTo=0;
	public static boolean usedCustom=false;
	public static short band_equi=0;
	
	

	public static SongInfo getSongInfo() {
		return songInfo;
	}

	public static void setSongInfo(SongInfo songInfo) {
		StaticMusic.songInfo = songInfo;
	}

	public static Music getMusic() {
		return music;
	}

	public static void setMusic(Music music) {
		StaticMusic.music = music;
	}

	public static LinkedList<SongInfo> getSongQueue() {
		return songQueue;
	}

	public static void setSongQueue(LinkedList<SongInfo> songQueue) {
		StaticMusic.songQueue = songQueue;
	}
	
	
	

}
