package com.mediaplayer.db;

import java.util.ArrayList;
import java.util.HashSet;

import com.mediaplayer.com.SongInfo;
import com.mediaplayer.utility.Util;

import android.R.array;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class SongInfoDatabase {
	public static final String KEY_ROWID = "rowid";
	public static final String KEY_ID = "id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_ARTIST = "artist";
	public static final String KEY_ALBUM = "album";
	public static final String KEY_DISPLAYNAME = "displayname";
	public static final String KEY_DATA = "data";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_ALBUMART = "albumart";
	public static final String KEY_ALBUMID = "albumid";
	public static final String DATABASE_NAME = "SongInfoDatabase";
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_TABLE = "SongInfoTable";

	private static final String PLAYLIST_KEY_ROWID = "rowid";
	private static final String PLAYLIST_KEY_ID = "id";
	private static final String PLAYLIST_KEY_PLAYLIST = "playlist";
	private static final String PLAYLIST_DATABASE_TABLE = "PlayListTable";
	public Context ourContext;
	public SQLiteDatabase ourDatabase;
	public DbHelp ourHelper;

	public SongInfoDatabase(Context c) {
		ourContext = c;
	}

	public static class DbHelp extends SQLiteOpenHelper {
		public DbHelp(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE  " + DATABASE_TABLE + " ( " + KEY_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_ID
					+ " TEXT NOT NULL, " + KEY_TITLE + " TEXT NOT NULL, "
					+ KEY_ARTIST + " TEXT NOT NULL, " + KEY_DISPLAYNAME
					+ " TEXT NOT NULL, " + KEY_DURATION + " TEXT NOT NULL, "
					+ KEY_ALBUM + " TEXT NOT NULL, " + KEY_ALBUMID
					+ " TEXT NOT NULL, " + KEY_DATA + " TEXT NOT NULL, "
					+ KEY_ALBUMART + " TEXT NOT NULL " + " );");

			db.execSQL("CREATE TABLE  " + PLAYLIST_DATABASE_TABLE + " ( "
					+ PLAYLIST_KEY_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + PLAYLIST_KEY_ID
					+ " TEXT NOT NULL, " + PLAYLIST_KEY_PLAYLIST
					+ " TEXT NOT NULL " + " );");
			//Log.i("DATABASE", "CREATED");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + PLAYLIST_DATABASE_TABLE);
			onCreate(db);
		}
	}

	public SongInfoDatabase open() {
		ourHelper = new DbHelp(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		ourHelper.close();
	}

	public void Insert(SongInfo songInfo) {
		// Inserting User Input Values to Database
		ContentValues cv = new ContentValues();
		cv.put(KEY_ID, songInfo.getId());
		cv.put(KEY_TITLE, songInfo.getTitle());
		cv.put(KEY_ARTIST, songInfo.getArtist());
		cv.put(KEY_ALBUM, songInfo.getAlbum());
		cv.put(KEY_ALBUMART, songInfo.getAlbum_art());
		cv.put(KEY_ALBUMID, songInfo.getAlbum_id());
		cv.put(KEY_DATA, songInfo.getData());
		cv.put(KEY_DISPLAYNAME, songInfo.getDisplayName().trim());
		cv.put(KEY_DURATION, songInfo.getDuration());

		long inserted = ourDatabase.insert(DATABASE_TABLE, null, cv);


	}

	public void update(SongInfo songInfo) {
		ContentValues cv = new ContentValues();
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
				+ songInfo.getData() + "'", null);
	}

	public Cursor getTableCursor() {
		Cursor cursor;
		String query = "SELECT * from " + DATABASE_TABLE + " ORDER BY UPPER("
				+ KEY_TITLE + ");";
		cursor = ourDatabase.rawQuery(query, null);
		return cursor;
	}

	public ArrayList<SongInfo> getFullList() {

		ArrayList<SongInfo> songInfo;
		songInfo = new ArrayList<SongInfo>();
		SongInfo item = new SongInfo();
		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 AND LOWER("
				+ MediaStore.Audio.Media.DISPLAY_NAME
				+ ") NOT LIKE  LOWER('%.wma') ";
		String sortOrder = "UPPER(" + MediaStore.Audio.Media.TITLE + ")";
		String[] projection = { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.ALBUM,
				MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.DURATION,
				MediaStore.Audio.Media.ALBUM_ID, };

		Cursor c =  ourContext.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
				selection, null, sortOrder);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			item = new SongInfo();
			item.setAlbum(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
			item.setAlbum_art(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
			item.setAlbum_id(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
			item.setArtist(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
			item.setData(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA)));
			item.setDisplayName(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
			item.setDuration(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DURATION)));
			item.setId(c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID)));
			item.setTitle(c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE)));
			songInfo.add(item);
		}
		c.close();
		return songInfo;
	}

	public SongInfo getNextSong(SongInfo cur_songInfo) {
		SongInfo songInfo = new SongInfo();
		/*
		 * String query="SELECT * FROM " + DATABASE_TABLE + " WHERE "+ KEY_ROWID
		 * + " = (SELECT MIN("+KEY_ROWID+") FROM " + DATABASE_TABLE +" WHERE " +
		 * KEY_ROWID
		 * +" > "+"(SELECT "+KEY_ROWID+" FROM "+DATABASE_TABLE+" WHERE "
		 * +KEY_DATA+" = '"+cur_songInfo.getData()+"') ) ;";
		 */

		String query = "SELECT * FROM " + DATABASE_TABLE + " WHERE LOWER("
				+ KEY_TITLE + ") > '"
				+ cur_songInfo.getTitle().replace("'", "''").toLowerCase()
				+ "' ORDER BY LOWER(" + KEY_TITLE + ") LIMIT 1;";

		try {
			Cursor c = ourDatabase.rawQuery(query, null);
			//Log.i("QUERY", query);
			if (c.getCount() > 0) {
				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					songInfo.setAlbum(c.getString(c.getColumnIndex(KEY_ALBUM)));
					songInfo.setAlbum_art(c.getString(c
							.getColumnIndex(KEY_ALBUMART)));
					songInfo.setAlbum_id(c.getString(c
							.getColumnIndex(KEY_ALBUMID)));
					songInfo.setArtist(c.getString(c.getColumnIndex(KEY_ARTIST)));
					songInfo.setData(c.getString(c.getColumnIndex(KEY_DATA)));
					songInfo.setDisplayName(c.getString(c
							.getColumnIndex(KEY_DISPLAYNAME)));
					songInfo.setDuration(c.getString(c
							.getColumnIndex(KEY_DURATION)));
					songInfo.setId(c.getString(c.getColumnIndex(KEY_ID)));
					songInfo.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
				}
				c.close();
			} else {
				query = "SELECT * FROM " + DATABASE_TABLE + " ORDER BY LOWER("
						+ KEY_TITLE + ") LIMIT 1;";
				//Log.i("QUERY", query);
				c = ourDatabase.rawQuery(query, null);
				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					songInfo.setAlbum(c.getString(c.getColumnIndex(KEY_ALBUM)));
					songInfo.setAlbum_art(c.getString(c
							.getColumnIndex(KEY_ALBUMART)));
					songInfo.setAlbum_id(c.getString(c
							.getColumnIndex(KEY_ALBUMID)));
					songInfo.setArtist(c.getString(c.getColumnIndex(KEY_ARTIST)));
					songInfo.setData(c.getString(c.getColumnIndex(KEY_DATA)));
					songInfo.setDisplayName(c.getString(c
							.getColumnIndex(KEY_DISPLAYNAME)));
					songInfo.setDuration(c.getString(c
							.getColumnIndex(KEY_DURATION)));
					songInfo.setId(c.getString(c.getColumnIndex(KEY_ID)));
					songInfo.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
				}
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return songInfo;
	}

	public SongInfo nextShuffleSong() {
		SongInfo songInfo = new SongInfo();
		// String omissions = Util.toSqlArray(played_songs);
		/*
		 * String query = "SELECT * FROM " + DATABASE_TABLE +
		 * " ORDER BY RAND() LIMIT 1 WHERE " + KEY_ID + " NOT IN (" + omissions
		 * + ");";
		 */
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
	}

	public ArrayList<ArrayList<SongInfo>> getSongs_allPlayList() {
		// String query="SELECT * FROM "+SongInfoDatabase.DATABASE_TABLE+
		// " song, "+DATABASE_TABLE +" obj WHERE song.id = obj.id";
		String query = "SELECT " + PLAYLIST_KEY_PLAYLIST + " FROM "
				+ PLAYLIST_DATABASE_TABLE + " GROUP BY "
				+ PLAYLIST_KEY_PLAYLIST + " ORDER BY " + PLAYLIST_KEY_PLAYLIST
				+ "; ";
		Cursor c = ourDatabase.rawQuery(query, null);
		ArrayList<String> playlists = new ArrayList<String>();
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			playlists.add(c.getString(c.getColumnIndex(PLAYLIST_KEY_PLAYLIST)));

		}
		c.close();
		ArrayList<ArrayList<SongInfo>> songInfo_array = new ArrayList<ArrayList<SongInfo>>();
		songInfo_array = getSongsForPlaylists(playlists);

		return songInfo_array;
	}

	private ArrayList<ArrayList<SongInfo>> getSongsForPlaylists(
			ArrayList<String> playlists) {
		// TODO Auto-generated method stub
		ArrayList<ArrayList<SongInfo>> songInfo_array = new ArrayList<ArrayList<SongInfo>>();
		ArrayList<SongInfo> temp_info = new ArrayList<SongInfo>();
		SongInfo item;
		for (int x = 0; x < playlists.size(); x++) {
			temp_info = new ArrayList<SongInfo>();
			String query = "SELECT  " + PLAYLIST_KEY_ID + " FROM "
					+ PLAYLIST_DATABASE_TABLE + " WHERE "
					+ PLAYLIST_KEY_PLAYLIST + " = '" + playlists.get(x) + "'"
					+ "; ";
			//Log.i("Playlist Query", query);
			Cursor c;
			Cursor c1 = ourDatabase.rawQuery(query, null);

			// adding group of items of a particular playlist to an array
			for (c1.moveToFirst(); !c1.isAfterLast(); c1.moveToNext()) {
				//Log.i("query", "Before query formatio for selecting ID");
				query = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_ID
						+ "="
						+ c1.getString(c1.getColumnIndex(PLAYLIST_KEY_ID))
						+ ";";

				c = ourDatabase.rawQuery(query, null);

				// adding single item to object
				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					//Log.i("Before adding", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					item = new SongInfo();
					item.setAlbum(c.getString(c.getColumnIndex(KEY_ALBUM)));
					item.setAlbum_art(c.getString(c
							.getColumnIndex(KEY_ALBUMART)));
					item.setAlbum_id(c.getString(c.getColumnIndex(KEY_ALBUMID)));
					item.setArtist(c.getString(c.getColumnIndex(KEY_ARTIST)));
					item.setData(c.getString(c.getColumnIndex(KEY_DATA)));
					item.setDisplayName(c.getString(c
							.getColumnIndex(KEY_DISPLAYNAME)));
					item.setDuration(c.getString(c.getColumnIndex(KEY_DURATION)));
					item.setId(c.getString(c.getColumnIndex(KEY_ID)));
					item.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
					item.setPlaylist(playlists.get(x));
					temp_info.add(item);
					//Log.i("Item added", item.getDisplayName());

				}
				c.close();
				// //Log.i("Adding to temp info",
				// temp_info.get(x).getDisplayName());

			}
			c1.close();
			songInfo_array.add(temp_info);

		}

		return songInfo_array;
	}

	public void addToPlaylist(String id, String playlist) {
		// Inserting User Input Values to Database
		ContentValues cv = new ContentValues();
		cv.put(PLAYLIST_KEY_ID, id);
		cv.put(PLAYLIST_KEY_PLAYLIST, playlist);
		if (checkDuplication(id.trim(), playlist.trim())) {
			long inserted = ourDatabase.insert(PLAYLIST_DATABASE_TABLE, null,
					cv);
			//Log.d("Play LIST", "Play  LIST INSERTED:::::" + inserted);
		} else
			Toast.makeText(ourContext, "Already in the Current playlist",
					Toast.LENGTH_LONG).show();

	}

	public void deletePlaylist(String playlist) {
		ourDatabase.delete(PLAYLIST_DATABASE_TABLE, PLAYLIST_KEY_PLAYLIST
				+ "='" + playlist + "'", null);
		//Log.i("DATABASE", "DELETED");
	}

	private boolean checkDuplication(String id, String playlist) {
		// TODO Auto-generated method stub
		String query = "SELECT * FROM " + PLAYLIST_DATABASE_TABLE
				+ " WHERE LOWER(" + PLAYLIST_KEY_ID + ") = LOWER('" + id
				+ "') AND LOWER(" + PLAYLIST_KEY_PLAYLIST + ") = LOWER('"
				+ playlist + "');";
		Cursor c = ourDatabase.rawQuery(query, null);
		if (c.moveToNext()) {
			return false;
		}
		c.close();
		return true;
	}

	public ArrayList<String> getAllPlaylists() {
		ArrayList<String> playlists = new ArrayList<String>();
		String query = "SELECT " + PLAYLIST_KEY_PLAYLIST + " FROM "
				+ PLAYLIST_DATABASE_TABLE + " GROUP BY "
				+ PLAYLIST_KEY_PLAYLIST + "; ";
		Cursor c = ourDatabase.rawQuery(query, null);
		playlists = new ArrayList<String>();
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			playlists.add(c.getString(c.getColumnIndex(PLAYLIST_KEY_PLAYLIST)));

		}
		c.close();
		return playlists;
	}

	public ArrayList<SongInfo> searchSong_byName(String search) {
		ArrayList<SongInfo> array_songInfo = new ArrayList<SongInfo>();
		SongInfo songInfo = new SongInfo();
		String query = "SELECT * FROM " + DATABASE_TABLE + " WHERE LOWER("
				+ KEY_TITLE + ") LIKE LOWER('" + search + "%') ORDER BY UPPER("
				+ KEY_TITLE + ") ;";
		Cursor c = ourDatabase.rawQuery(query, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			songInfo = new SongInfo();
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
			array_songInfo.add(songInfo);
		}
		c.close();
		return array_songInfo;
	}

	public ArrayList<ArrayList<SongInfo>> getSongs_albums() {
		String query = "SELECT " + KEY_ALBUM + " FROM " + DATABASE_TABLE
				+ " GROUP BY " + KEY_ALBUM + " ORDER BY " + KEY_ALBUM + "; ";
		Cursor c = ourDatabase.rawQuery(query, null);
		ArrayList<String> albums = new ArrayList<String>();
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			albums.add(c.getString(c.getColumnIndex(KEY_ALBUM)));
			//Log.i("ALBUMS", c.getString(c.getColumnIndex(KEY_ALBUM)));

		}
		c.close();
		ArrayList<ArrayList<SongInfo>> songInfo_array = new ArrayList<ArrayList<SongInfo>>();
		songInfo_array = getSongsForAlbums(albums);

		return songInfo_array;

	}

	private ArrayList<ArrayList<SongInfo>> getSongsForAlbums(
			ArrayList<String> albums) {
		// TODO Auto-generated method stub
		ArrayList<ArrayList<SongInfo>> songInfo_array = new ArrayList<ArrayList<SongInfo>>();
		ArrayList<SongInfo> temp_info = new ArrayList<SongInfo>();
		SongInfo item;
		for (int x = 0; x < albums.size(); x++) {
			temp_info = new ArrayList<SongInfo>();
			String query = "SELECT  * FROM " + DATABASE_TABLE + " WHERE "
					+ KEY_ALBUM + " = '" + albums.get(x).replace("'", "''")
					+ "'" + "; ";
			//Log.i("Playlist Query", query);
			Cursor c1 = ourDatabase.rawQuery(query, null);
			for (c1.moveToFirst(); !c1.isAfterLast(); c1.moveToNext()) {
				item = new SongInfo();
				item.setAlbum(c1.getString(c1.getColumnIndex(KEY_ALBUM)));
				item.setAlbum_art(c1.getString(c1.getColumnIndex(KEY_ALBUMART)));
				item.setAlbum_id(c1.getString(c1.getColumnIndex(KEY_ALBUMID)));
				item.setArtist(c1.getString(c1.getColumnIndex(KEY_ARTIST)));
				item.setData(c1.getString(c1.getColumnIndex(KEY_DATA)));
				item.setDisplayName(c1.getString(c1
						.getColumnIndex(KEY_DISPLAYNAME)));
				item.setDuration(c1.getString(c1.getColumnIndex(KEY_DURATION)));
				item.setId(c1.getString(c1.getColumnIndex(KEY_ID)));
				item.setTitle(c1.getString(c1.getColumnIndex(KEY_TITLE)));
				item.setPlaylist(item.getAlbum());
				temp_info.add(item);
			}
			songInfo_array.add(temp_info);
		}
		return songInfo_array;
	}

	public ArrayList<ArrayList<SongInfo>> getSongs_artists() {
		String query = "SELECT " + KEY_ARTIST + " FROM " + DATABASE_TABLE
				+ " GROUP BY " + KEY_ARTIST + " ORDER BY " + KEY_ARTIST + "; ";
		Cursor c = ourDatabase.rawQuery(query, null);
		ArrayList<String> artists = new ArrayList<String>();
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			artists.add(c.getString(c.getColumnIndex(KEY_ARTIST)));
			//Log.i("Artist", c.getString(c.getColumnIndex(KEY_ARTIST)));

		}
		c.close();
		ArrayList<ArrayList<SongInfo>> songInfo_array = new ArrayList<ArrayList<SongInfo>>();
		songInfo_array = getSongsForArtists(artists);
		return songInfo_array;

	}

	private ArrayList<ArrayList<SongInfo>> getSongsForArtists(
			ArrayList<String> artists) {
		// TODO Auto-generated method stub
		ArrayList<ArrayList<SongInfo>> songInfo_array = new ArrayList<ArrayList<SongInfo>>();
		ArrayList<SongInfo> temp_info = new ArrayList<SongInfo>();
		SongInfo item;
		for (int x = 0; x < artists.size(); x++) {
			temp_info = new ArrayList<SongInfo>();
			String query = "SELECT  * FROM " + DATABASE_TABLE + " WHERE "
					+ KEY_ARTIST + " = '" + artists.get(x).replace("'", "''")
					+ "'" + "; ";
			//Log.i("Artist Query", query);
			Cursor c1 = ourDatabase.rawQuery(query, null);
			for (c1.moveToFirst(); !c1.isAfterLast(); c1.moveToNext()) {
				item = new SongInfo();
				item.setAlbum(c1.getString(c1.getColumnIndex(KEY_ALBUM)));
				item.setAlbum_art(c1.getString(c1.getColumnIndex(KEY_ALBUMART)));
				item.setAlbum_id(c1.getString(c1.getColumnIndex(KEY_ALBUMID)));
				item.setArtist(c1.getString(c1.getColumnIndex(KEY_ARTIST)));
				item.setData(c1.getString(c1.getColumnIndex(KEY_DATA)));
				item.setDisplayName(c1.getString(c1
						.getColumnIndex(KEY_DISPLAYNAME)));
				item.setDuration(c1.getString(c1.getColumnIndex(KEY_DURATION)));
				item.setId(c1.getString(c1.getColumnIndex(KEY_ID)));
				item.setTitle(c1.getString(c1.getColumnIndex(KEY_TITLE)));
				item.setPlaylist(item.getArtist());
				temp_info.add(item);
			}
			songInfo_array.add(temp_info);
		}
		return songInfo_array;
	}

	public ArrayList<ArrayList<SongInfo>> searchSongs_albums(String search) {
		ArrayList<String> albums = new ArrayList<String>();
		String query = "SELECT " + KEY_ALBUM + " FROM " + DATABASE_TABLE
				+ " WHERE " + KEY_ALBUM + " LIKE '" + search.replace("'", "''")
				+ "%" + "' ; ";
		Cursor c = ourDatabase.rawQuery(query, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			albums.add(c.getString(c.getColumnIndex(KEY_ALBUM)));
			//Log.i("ALBUMS", c.getString(c.getColumnIndex(KEY_ALBUM)));

		}
		albums = new ArrayList<String>(new HashSet<String>(albums));
		c.close();
		ArrayList<ArrayList<SongInfo>> songInfo_array = new ArrayList<ArrayList<SongInfo>>();
		songInfo_array = getSongsForAlbums(albums);

		return songInfo_array;
	}

	public ArrayList<ArrayList<SongInfo>> searchSongs_artists(String search) {
		ArrayList<String> albums = new ArrayList<String>();
		String query = "SELECT " + KEY_ARTIST + " FROM " + DATABASE_TABLE
				+ " WHERE " + KEY_ARTIST + " LIKE '"
				+ search.replace("'", "''") + "%" + "' ; ";
		Cursor c = ourDatabase.rawQuery(query, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			albums.add(c.getString(c.getColumnIndex(KEY_ARTIST)));
			//Log.i("KEY_ARTIST", c.getString(c.getColumnIndex(KEY_ARTIST)));

		}
		albums = new ArrayList<String>(new HashSet<String>(albums));
		c.close();
		ArrayList<ArrayList<SongInfo>> songInfo_array = new ArrayList<ArrayList<SongInfo>>();
		songInfo_array = getSongsForArtists(albums);

		return songInfo_array;
	}

	public ArrayList<ArrayList<SongInfo>> searchSongs_playlists(String search) {
		ArrayList<String> playlists = new ArrayList<String>();
		String query = "SELECT " + PLAYLIST_KEY_PLAYLIST + " FROM "
				+ PLAYLIST_DATABASE_TABLE + " WHERE " + PLAYLIST_KEY_PLAYLIST
				+ " LIKE '" + search.replace("'", "''") + "%" + "' ; ";
		Cursor c = ourDatabase.rawQuery(query, null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			playlists.add(c.getString(c.getColumnIndex(PLAYLIST_KEY_PLAYLIST)));

		}
		playlists = new ArrayList<String>(new HashSet<String>(playlists));
		c.close();
		ArrayList<ArrayList<SongInfo>> songInfo_array = new ArrayList<ArrayList<SongInfo>>();
		songInfo_array = getSongsForPlaylists(playlists);

		return songInfo_array;
	}

	public void deleteOneSong(String songData) {

		try {
			ourDatabase.delete(DATABASE_TABLE, KEY_DATA + " = '"
					+ songData.trim().replace("'", "''") + "'", null);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean isOpen() {
		return ourDatabase.isOpen();
	}
}
