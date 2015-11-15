package com.mediaplayer.com;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.io.Serializable;

public class SongInfo  implements Serializable{

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

	public SongInfo(Cursor c) {
		super();
		this.setAlbum(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
		this.setAlbum_art(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
		this.setAlbum_id(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
		this.setArtist(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
		this.setData(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA)));
		this.setDisplayName(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
		this.setDuration(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DURATION)));
		this.setId(c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID)));
		this.setTitle(c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE)));

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

	@Override
	public boolean equals(Object o) {
		return (this.getId() == ((SongInfo)o).getId()) && (this.hashCode() == o.hashCode());
	}
}
