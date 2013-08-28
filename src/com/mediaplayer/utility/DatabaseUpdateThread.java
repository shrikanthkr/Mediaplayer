package com.mediaplayer.utility;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.location.GpsStatus.Listener;
import android.provider.MediaStore;
import android.util.Log;

import com.mediaplayer.com.SongInfo;
import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.listener.OnDatabaseChangeListener;

public class DatabaseUpdateThread extends Thread {

	SongInfoDatabase database;
	Cursor myDbCursor, mediastoreCursor;
	CursorJoiner joiner;
	OnDatabaseChangeListener mListener;
	String[] mydbColumns = new String[1];
	String[] mediaStoreDBColumns = new String[1];
	SongInfo songInfo;
	Context context;
	boolean changed;

	public DatabaseUpdateThread(Context context,
			OnDatabaseChangeListener listener) {
		// TODO Auto-generated constructor stub
		database = new SongInfoDatabase(context);
		database.open();
		myDbCursor = database.getTableCursor();
		this.context = context;
		getMediStoreCursor();
		mydbColumns[0] = SongInfoDatabase.KEY_DATA;
		mediaStoreDBColumns[0] = MediaStore.Audio.Media.DATA;
		Log.i("Update thread", "got mydb cursor::" + mediaStoreDBColumns[0]);
		joiner = new CursorJoiner(myDbCursor, mydbColumns,
				this.mediastoreCursor, mediaStoreDBColumns);
		mListener = listener;
		changed = false;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		for (CursorJoiner.Result joinerResult : joiner) {
			switch (joinerResult) {
			case LEFT:
				// handle case where a row in cursorA is unique
				Log.i("DB UPDATE THREAD", "DELETEINGGGG");
								database.deleteOneSong(myDbCursor.getString(myDbCursor
							.getColumnIndex(SongInfoDatabase.KEY_DATA)));
				changed = true;
				break;
			case RIGHT:
				// handle case where a row in cursorB is unique
				songInfo = new SongInfo();

				try {
					songInfo.setAlbum(mediastoreCursor.getString(mediastoreCursor
							.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
				} catch (Exception e) {
					songInfo.setAlbum("NIL");
				}
				try {
					songInfo.setAlbum_id(mediastoreCursor.getString(mediastoreCursor
							.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
				} catch (Exception e) {
					songInfo.setAlbum_id("nil");
				}
				try {
					songInfo.setArtist(mediastoreCursor.getString(mediastoreCursor
							.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
				} catch (Exception e) {
					songInfo.setArtist("NIL");
				}
				try {
					songInfo.setAlbum_art("nil");
				} catch (Exception e) {
				}
				try {
					songInfo.setData(mediastoreCursor.getString(mediastoreCursor
							.getColumnIndex(MediaStore.Audio.Media.DATA)));
				} catch (Exception e) {
					songInfo.setData("nil");
				}
				try {
					songInfo.setDisplayName(mediastoreCursor.getString(mediastoreCursor
							.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
				} catch (Exception e) {
					songInfo.setDisplayName("nil");
				}
				try {
					songInfo.setDuration(mediastoreCursor.getString(mediastoreCursor
							.getColumnIndex(MediaStore.Audio.Media.DURATION)));
				} catch (Exception e) {
					songInfo.setDuration("0");
				}
				try {
					songInfo.setId(mediastoreCursor.getString(mediastoreCursor
							.getColumnIndex(MediaStore.Audio.Media._ID)));
				} catch (Exception e) {
					songInfo.setId("nil");
				}
				try {
					songInfo.setTitle(mediastoreCursor.getString(mediastoreCursor
							.getColumnIndex(MediaStore.Audio.Media.TITLE)));
				} catch (Exception e) {
					songInfo.setTitle("title");
				}
		
					database.Insert(songInfo);
				changed = true;
				break;
			case BOTH:
				// handle case where a row with the same key is in both cursors
				break;
			}
		}
		if (changed) {
			Log.i("DB UPDATE THREAD", "FOUND UPDATES");
			mListener.onChange();
		}
	}

	public void getMediStoreCursor() {
		// TODO Auto-generated method stub
		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 AND LOWER("
				+ MediaStore.Audio.Media.DISPLAY_NAME
				+ ") NOT LIKE  LOWER('%.wma') ";
		String sortOrder = "UPPER(" + MediaStore.Audio.Media.TITLE + ")";
		String[] projection = { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
				MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.DURATION,
				MediaStore.Audio.Media.ALBUM_ID, };

		mediastoreCursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
				selection, null, sortOrder);
		Log.i("String Projection", projection[0]);
		mediastoreCursor.moveToNext();
		Log.i("String Projection", projection[1]);
	}
}
