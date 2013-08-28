package com.mediaplayer.com;

import java.io.Serializable;

public class SongInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 123;
	public String title;
	public String artist;
	public String album;
	public String id;
	public String displayName;
	public String data;
	public String duration;
	public String album_art;
	public String album_id;
	String playlist;

	public String getPlaylist() {
		return playlist;
	}

	public void setPlaylist(String playlist) {
		this.playlist = playlist;
	}

	public SongInfo() {
		super();
	}

	public String getAlbum_id() {
		return album_id;
	}

	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}

	public String getAlbum_art() {
		return album_art;
	}

	public void setAlbum_art(String album_art) {
		this.album_art = album_art;
	}

	public SongInfo(String title, String artist, String album, String id,
			String displayName, String data, String duration) {
		super();
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.id = id;
		this.displayName = displayName;
		this.data = data;
		this.duration = duration;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
}
