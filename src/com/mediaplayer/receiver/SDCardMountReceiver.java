package com.mediaplayer.receiver;

import java.util.ArrayList;
import java.util.HashMap;

import com.mediaplayer.com.SongInfo;
import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.listener.OnDatabaseChangeListener;
import com.mediaplayer.utility.DatabaseUpdateThread;
import com.mediaplayer.utility.Util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class SDCardMountReceiver extends BroadcastReceiver {

	Util util;
	SongInfoDatabase database;
	ArrayList<SongInfo> songList;
	DatabaseUpdateThread databaseUpdateThread;

	public SDCardMountReceiver() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		/*
		 * database = new SongInfoDatabase(context); database.open();
		 * database.delete(); util = new Util(); songList =
		 * util.getAllmusic(context,context.getContentResolver());
		 * Log.i("RECEIVER", "MOUNTED"); SongInfo item; for (int i = 0; i <
		 * songList.size(); i++) { item = new SongInfo(); item =
		 * songList.get(i); database.Insert(item); } database.close();
		 */
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
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
				selection, null, sortOrder);
		databaseUpdateThread = new DatabaseUpdateThread(context, 
				new OnDatabaseChangeListener() {

					@Override
					public void onChange() {
						// TODO Auto-generated method stub
						Log.i("MEDIAPLAYER ", "DATABASE UPDATED");
					}
				});
		try {
			if (databaseUpdateThread.isAlive()) {
				databaseUpdateThread.interrupt();
			}
			databaseUpdateThread.run();

		} catch (Exception e) {
			e.printStackTrace();
			databaseUpdateThread.start();
		}

	}

}