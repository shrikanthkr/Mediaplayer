package com.mediaplayer.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.mediaplayer.adapter.CommonListAdapter;
import com.mediaplayer.adapter.SongsListAdapter;
import com.mediaplayer.adapter.SongsListAdapter.ViewHolder;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.listener.PlaylistChangedListener;
import com.mediaplayer.manager.BroadcastManager;
import com.mediaplayer.utility.DatabaseUpdateThread;
import com.mediaplayer.utility.SongsHolder;
import com.mediaplayer.utility.Util;

public class SongListFragment extends MediaFragment implements
OnGestureListener, SearchView.OnQueryTextListener {
	ListView lv;
	SearchView searchView;
	Context context;
	SongsListAdapter adapter;
	ArrayList<SongInfo> songList;
	GestureDetector detector;
	SongInfoDatabase database;
	Activity activity;
	Util util;
	ArrayList<ArrayList<SongInfo>> all_playlists;
	CommonListAdapter common_list_adapter;
	final int SONG_VIEW = 0;
	final int ARTIST_VIEW = 1;
	final int PLAYLIST_VIEW = 2;
	final int ALBUMS_VIEW = 3;
	static int SWITCH_VIEW = 0;
	DatabaseUpdateThread databaseUpdateThread;
	Cursor cursor;
	PlaylistChangedListener playlistChangedListener;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		util = new Util();
		songList = new ArrayList<SongInfo>();
		context = getActivity();
		activity = getActivity();
		database = new SongInfoDatabase(context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.songlistfragment_xml,container,false);
		lv = (ListView) v.findViewById(R.id.listView);
/*		search_edittext = (EditText) v.findViewById(R.id.search_edittext);
		search_edittext.setVisibility(View.INVISIBLE);*/
		//search_database_thread = new DatabaseThread();
		/*search_edittext.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

				search_database_thread.setSearch(s.toString());
				if (search_database_thread.isAlive()) {
					search_database_thread.interrupt();
				}
				try {
					search_database_thread.run();
				} catch (Exception r) {
					r.printStackTrace();
					search_database_thread.start();
				}

			}
		});*/
		populateSonglist();
		return v;
	}

	public void populateSonglist() {
		SWITCH_VIEW = SONG_VIEW;
		database.open();
		songList = database.getFullList();
		database.close();
		detector = new GestureDetector(getActivity(), this);
		adapter = new SongsListAdapter(getActivity(), songList, lv);
		lv.setTextFilterEnabled(true);

		lv.setAdapter(adapter);
		lv.setSelection(SongsHolder.smoothScrollTo);
		lv.setFastScrollEnabled(true);
		lv.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View view, MotionEvent e) {
				detector.onTouchEvent(e);
				return false;
			}
		});
	}


	/*@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.song_search_button:

			song_search_button.setVisibility(View.INVISIBLE);
			search_edittext.setVisibility(View.VISIBLE);
			search_edittext.requestFocus();
			search_edittext.dispatchTouchEvent(MotionEvent.obtain(
					SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
					MotionEvent.ACTION_DOWN, 0, 0, 0));
			search_edittext.dispatchTouchEvent(MotionEvent.obtain(
					SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
					MotionEvent.ACTION_UP, 0, 0, 0));
			break;
		}

	}*/



	private void removeGestureListener() {
		// TODO Auto-generated method stub
		lv.setOnTouchListener(null);

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SongsHolder.smoothScrollTo = lv.getFirstVisiblePosition();
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float arg2,
			float arg3) {
			return true;
	}

	@Override
	public void onLongPress(MotionEvent e1) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e1) {
		// TODO Auto-generated method stub
		SongInfo songInfo = new SongInfo();
		int id = lv.pointToPosition((int) e1.getX(), (int) e1.getY());
		String path = songList.get(id).getData();
		long duration = Long.parseLong(songList.get(id).getDuration());
		//Log.i("SONG LIST FRAGMENT", songList.get(id).getTitle());

		songInfo.setAlbum(songList.get(id).getAlbum());
		songInfo.setAlbum_art(songList.get(id).getAlbum_art());
		songInfo.setAlbum_id(songList.get(id).getAlbum_id());
		songInfo.setArtist(songList.get(id).getArtist());
		songInfo.setData(songList.get(id).getData());
		songInfo.setDisplayName(songList.get(id).getDisplayName());
		songInfo.setDuration(songList.get(id).getDuration());
		songInfo.setId(songList.get(id).getId());
		songInfo.setTitle(songList.get(id).getTitle());
		Intent playSong = new Intent(BroadcastManager.PLAYSONG);
		Bundle b= new Bundle();
		b.putSerializable(BroadcastManager.SONG_KEY, songInfo);
		playSong.putExtras(b);
		LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(playSong);
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String s) {
		searchSongs(s);
		return false;
	}

	@Override
	public boolean onQueryTextChange(String s) {
		searchSongs(s);
		return false;
	}

	private void searchSongs(String search){
		database.open();
		songList = database.searchSong_byName(search);
		adapter.addAll(songList);
		database.close();
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		searchView = (SearchView)menu.findItem(R.id.search).getActionView();
		searchView.setOnQueryTextListener(this);
	}
}
