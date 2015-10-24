package com.mediaplayer.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.mediaplayer.com.MetaInfo;
import com.mediaplayer.com.MyApplication;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.utility.Util;

import android.R.array;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class SongInfoDatabase {
	public static Context ourContext;
	static SongInfoDatabase db;

	public static SongInfoDatabase getInstance() {
		ourContext = MyApplication.getContext();
		if(db==null) db = new SongInfoDatabase();
		return db;
	}

	public void open() {
	}

	public void close() {
	}

	public void update(SongInfo songInfo) {
		/*ContentValues cv = new ContentValues();
		cv.put(KEY_ID, songInfo.getId());
		cv.put(KEY_TITLE, songInfo.getTitle());
		cv.put(KEY_ARTIST, songInfo.getArtist());
		cv.put(KEY_ALBUM, songInfo.getAlbum());
		cv.put(KEY_ALBUMART, songInfo.getAlbum_art());
		cv.put(KEY_ALBUMID, songInfo.getAlbum_id());
		cv.put(KEY_DATA, songInfo.getData());
		cv.put(KEY_DISPLAYNAME, songInfo.getDisplayName().trim());
		cv.put(KEY_DURATION, songInfo.getDuration());
		long inserted = ourDatabase.update(DATABASE_TABLE, cv, KEY_DATA + "='"
				+ songInfo.getData() + "'", null);*/
	}




	private Cursor getMediaStoreCursor(String additional, String limit) {
		StringBuilder selection = new StringBuilder();
		String sortOrder = "LOWER(" + MediaStore.Audio.Media.TITLE + ")";
		selection.append("( " + MediaStore.Audio.Media.IS_MUSIC + " != 0 AND LOWER("
				+ MediaStore.Audio.Media.DISPLAY_NAME
				+ ") NOT LIKE  LOWER('%.wma') ");
		if (additional != null && additional.length() > 0) {
			selection.append("AND " + additional);
		}
		selection.append(" )");
		if(limit!=null && limit.length() > 0 ){
			limit = " LIMIT " + limit ;
		}else{
			limit = "";
		}
		String[] projection = {MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.ALBUM,
				MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.DURATION,
				MediaStore.Audio.Media.ALBUM_ID,};
		Cursor c = ourContext.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
				selection.toString(), null, sortOrder + limit);
		return c;
	}

	public ArrayList<SongInfo> getSongs(String search) {
		ArrayList<SongInfo> songInfo;
		songInfo = new ArrayList<>();
		SongInfo item;
		String query = null;
		if(search!=null && search.length() > 0){
			query = "LOWER("
					+ MediaStore.Audio.Media.TITLE + ") LIKE LOWER('" + search + "%') ";
		}
		Cursor c = getMediaStoreCursor(query, "");

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			item = new SongInfo(c);
			songInfo.add(item);
		}
		c.close();
		return songInfo;
	}

	public SongInfo getNextSong(SongInfo cur_songInfo) {
		SongInfo songInfo = new SongInfo();
		String selection = "LOWER("
				+ MediaStore.Audio.Media.DISPLAY_NAME
				+ ") NOT LIKE  LOWER('%.wma')  "
				+ "AND LOWER("
				+ MediaStore.Audio.Media.TITLE + ") > '"
				+ cur_songInfo.getTitle().replace("'", "''").toLowerCase()
				+ "'";

		Cursor c = getMediaStoreCursor(selection, "1");
		//Log.i("QUERY", query);
		if (c.getCount() > 0) {
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				songInfo = new SongInfo(c);
			}
		} else {
			selection = "";
			c = getMediaStoreCursor(selection, "1");
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				songInfo = new SongInfo(c);
			}
		}
		c.close();
		return songInfo;
	}

	/*public SongInfo nextShuffleSong() {
		SongInfo songInfo = new SongInfo();
		// String omissions = Util.toSqlArray(played_songs);
		*//*
		 * String query = "SELECT * FROM " + DATABASE_TABLE +
		 * " ORDER BY RAND() LIMIT 1 WHERE " + KEY_ID + " NOT IN (" + omissions
		 * + ");";
		 *//*
		String query = "SELECT * FROM " + DATABASE_TABLE
				+ " ORDER BY RANDOM() LIMIT 1 ;";
		Cursor c = ourDatabase.rawQuery(query, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			songInfo.setAlbum(c.getString(c.getColumnIndex(KEY_ALBUM)));
			songInfo.setAlbum_art(c.getString(c.getColumnIndex(KEY_ALBUMART)));
			songInfo.setAlbum_id(c.getString(c.getColumnIndex(KEY_ALBUMID)));
			songInfo.setArtist(c.getString(c.getColumnIndex(KEY_ARTIST)));
			songInfo.setData(c.getString(c.getColumnIndex(KEY_DATA)));
			songInfo.setDisplayName(c.getString(c
					.getColumnIndex(KEY_DISPLAYNAME)));
			songInfo.setDuration(c.getString(c.getColumnIndex(KEY_DURATION)));
			songInfo.setId(c.getString(c.getColumnIndex(KEY_ID)));
			songInfo.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
		}
		c.close();
		return songInfo;
	}*/
	private Cursor getAllAlbums(String search) {
		ContentResolver contentResolver = ourContext.getContentResolver();
		String[] projection = {MediaStore.Audio.Albums._ID,
				MediaStore.Audio.Albums.ALBUM,
				MediaStore.Audio.Albums.ALBUM_ART,
				MediaStore.Audio.Albums.NUMBER_OF_SONGS};

		if (search != null && search.length() > 0) {
			search = "LOWER("
					+ MediaStore.Audio.Albums.ALBUM + ") LIKE LOWER('" + search + "%') ";
		}

		return contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
				projection,
				search,
				null,
				MediaStore.Audio.Albums.ALBUM + " ASC");

	}

	public ArrayList<MetaInfo> getAlbums(String search) {
		ArrayList<MetaInfo> infos = new ArrayList<>();
		Cursor c = getAllAlbums(search);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			MetaInfo info = new MetaInfo();
			info.setId(c.getString(c.getColumnIndex(MediaStore.Audio.Albums._ID)));
			info.setName(c.getString(c.getColumnIndex(MediaStore.Audio.Albums.ALBUM)));
			info.setNumber_of_songs(c.getString(c.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS)));
			infos.add(info);
		}
		c.close();
		return infos;

	}

	public ArrayList<SongInfo> getSongsForAlbum(MetaInfo info) {
		ArrayList<SongInfo> songInfo_array;
		SongInfo item;
		songInfo_array = new ArrayList<>();
		String query = MediaStore.Audio.Media.ALBUM_ID + " = '" + info.getId() + "'";
		Cursor c1 = getMediaStoreCursor(query, null);
		for (c1.moveToFirst(); !c1.isAfterLast(); c1.moveToNext()) {
			item = new SongInfo(c1);
			songInfo_array.add(item);
		}
		c1.close();
		return songInfo_array;
	}

	private Cursor getAllArtists(String search) {
		ContentResolver contentResolver = ourContext.getContentResolver();
		String[] projection = {MediaStore.Audio.Artists._ID,
				MediaStore.Audio.Artists.ARTIST,
				MediaStore.Audio.Artists.NUMBER_OF_TRACKS};
		if (search != null && search.length() > 0) {
			search = "LOWER("
					+ MediaStore.Audio.Albums.ARTIST + ") LIKE LOWER('" + search + "%') ";
		}

		return contentResolver.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
				projection,
				search,
				null,
				MediaStore.Audio.Artists.ARTIST + " ASC");

	}

	private Cursor getAlbumArtistCursor(String artistId){
		return ourContext.getContentResolver().query(MediaStore.Audio.Artists.Albums.getContentUri("external", Long.parseLong(artistId)),
				new String[]{
						MediaStore.Audio.Artists._ID,
						MediaStore.Audio.Albums.ALBUM_ID,
						MediaStore.Audio.Artists.ARTIST,
						MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
				}, null, null, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
	}

	public String getAlbumIdForArtist(String artistId){
		Cursor c = getAlbumArtistCursor(artistId);
		return c.getString(c.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID));
	}

	public ArrayList<MetaInfo> getArtists(String search) {
		ArrayList<MetaInfo> infos = new ArrayList<>();
		Cursor c = getAllArtists(search);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			MetaInfo info = new MetaInfo();
			info.setId(c.getString(c.getColumnIndex(MediaStore.Audio.Artists._ID)));
			info.setName(c.getString(c.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));
			info.setNumber_of_songs(c.getString(c.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS)));
			infos.add(info);
		}
		c.close();
		return infos;
	}

	public ArrayList<SongInfo> getSongsForArtist(MetaInfo info) {
		ArrayList<SongInfo> songInfo_array;
		SongInfo item;
		songInfo_array = new ArrayList<>();
		String query = MediaStore.Audio.Media.ARTIST_ID + " = '" + info.getId() + "'";
		Cursor c1 = getMediaStoreCursor(query, null);
		for (c1.moveToFirst(); !c1.isAfterLast(); c1.moveToNext()) {
			item = new SongInfo(c1);
			songInfo_array.add(item);
		}
		c1.close();
		return songInfo_array;
	}

	public String getAlbumIdForPlaylist(String artistId){
		Cursor c = getAlbumArtistCursor(artistId);
		return c.getString(c.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID));
	}

	private Cursor getAllPlaylist(String search) {
		ContentResolver contentResolver = ourContext.getContentResolver();
		String[] projection = {MediaStore.Audio.Playlists._ID,
				MediaStore.Audio.Playlists.NAME
		};
		if (search != null && search.length() > 0) {
			search = "LOWER("
					+ MediaStore.Audio.Playlists.NAME + ") LIKE LOWER('" + search + "%') ";
		}

		return contentResolver.query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
				projection,
				search,
				null,
				MediaStore.Audio.Playlists.NAME + " ASC");

	}

	public ArrayList<MetaInfo> getPLaylists(String search){
		ArrayList<MetaInfo> infos = new ArrayList<>();
		Cursor c = getAllPlaylist(search);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			MetaInfo info = new MetaInfo();
			info.setId(c.getString(c.getColumnIndex(MediaStore.Audio.Playlists._ID)));
			info.setName(c.getString(c.getColumnIndex(MediaStore.Audio.Playlists.NAME)));
			infos.add(info);
		}
		c.close();
		return infos;
	}

	private Cursor getPLaylistCursor(String playlistId){
		String[] projection = {
				MediaStore.Audio.Playlists.Members.AUDIO_ID,
				MediaStore.Audio.Playlists.Members.ARTIST,
				MediaStore.Audio.Playlists.Members.TITLE,
				MediaStore.Audio.Playlists.Members._ID



		};
		Cursor cursor = null;
		cursor = ourContext.getContentResolver().query(
				MediaStore.Audio.Playlists.Members.getContentUri("external",Long.parseLong(playlistId ) ),
				projection,
				MediaStore.Audio.Media.IS_MUSIC +" != 0 ",
				null,
				null);

	return cursor;
	}
	public ArrayList<SongInfo> getSongsForPlaylist(MetaInfo info) {
		ArrayList<SongInfo> songInfo_array;
		SongInfo item;
		songInfo_array = new ArrayList<>();
		Cursor c1 = getPLaylistCursor(info.getId());
		for (c1.moveToFirst(); !c1.isAfterLast(); c1.moveToNext()) {
			item = new SongInfo(c1);
			songInfo_array.add(item);
		}
		c1.close();
		return songInfo_array;
	}

	public void addToPlaylist(long[] songIds, long playlistId){
		for(int i=0;i<songIds.length;i++){
			addToPlaylist(songIds[i],playlistId);
		}
	}
	public  void addToPlaylist(long audioId, long playlistId) {

		String[] cols = new String[] {
				"count(*)"
		};
		Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
		Cursor cur = ourContext.getContentResolver().query(uri, cols, null, null, null);
		cur.moveToFirst();
		final int base = cur.getInt(0);
		cur.close();
		ContentValues values = new ContentValues();
		values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, base + audioId);
		values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, audioId);
		ourContext.getContentResolver().insert(uri, values);
	}

	public  void removeFromPlaylist( int playlistId, int audioId) {
		Log.v("made it to add", "" + audioId);
		String[] cols = new String[] {
				"count(*)"
		};
		Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
		Cursor cur = ourContext.getContentResolver().query(uri, cols, null, null, null);
		cur.moveToFirst();
		final int base = cur.getInt(0);
		cur.close();
		ourContext.getContentResolver().delete(uri, MediaStore.Audio.Playlists.Members.AUDIO_ID + " = " + audioId, null);
	}
	public void createNewPLaylist(long[] songIds,String name){
		int id = createPlaylist(name);
		addToPlaylist(songIds,id);
	}
	public int createPlaylist(String name){
		final String[] PROJECTION_PLAYLIST = new String[] {
				MediaStore.Audio.Playlists._ID,
				MediaStore.Audio.Playlists.NAME,
				MediaStore.Audio.Playlists.DATA
		};
		int mPlaylistId = 0;

		ContentValues mInserts = new ContentValues();
		mInserts.put(MediaStore.Audio.Playlists.NAME, name);
		mInserts.put(MediaStore.Audio.Playlists.DATE_ADDED, System.currentTimeMillis());
		mInserts.put(MediaStore.Audio.Playlists.DATE_MODIFIED, System.currentTimeMillis());
		Uri mUri = ourContext.getContentResolver().insert(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, mInserts);
		if (mUri != null) {
			Cursor c = ourContext.getContentResolver().query(mUri, PROJECTION_PLAYLIST, null, null, null);
			if (c != null) {
				// Save the newly created ID so it can be selected.  Names are allowed to be duplicated,
				// but IDs can never be.
				c.moveToFirst();
				mPlaylistId = c.getInt(c.getColumnIndex(MediaStore.Audio.Playlists._ID));
				c.close();
			}
		}
		return mPlaylistId;
	}
}
