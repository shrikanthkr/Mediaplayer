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
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

public class SongListFragment extends Fragment implements
OnGestureListener {
	ListView lv;
	SongsListAdapter adapter;
	ArrayList<SongInfo> songList;
	String path;
	GestureDetector detector;
	HashMap<String, Bitmap> art_work;
	Context context;
	SharedPreferences app_start;
	SharedPreferences.Editor app_start_editor;
	// SongInfo songInfo;
	long duration;
	SongInfoDatabase database;
	Activity activity;
	// ContentBody cbFile;
	Util util;
	//ProgressBar pb;
	ArrayList<ArrayList<SongInfo>> all_playlists;
	float downX = 0;
	float upX = 0;
	CommonListAdapter common_list_adapter;
	//EditText search_edittext;
	final int SONG_VIEW = 0;
	final int ARTIST_VIEW = 1;
	final int PLAYLIST_VIEW = 2;
	final int ALBUMS_VIEW = 3;
	static int SWITCH_VIEW = 0;
	Thread x;
	// UriObserver observer;
	//TextView songlist_header_textview;
	ImageView swipe_left, swipe_right, point_tut_imageview;
	TextView swipe_right_textview, swipe_left_textview, point_textview;
	DatabaseUpdateThread databaseUpdateThread;
	Cursor cursor;
	long previousTime = 0;
	PlaylistChangedListener playlistChangedListener;
	Button tut_button;
	AlphaAnimation alphaDown;
	AlphaAnimation alphaUp;
	boolean slidebutton_clicked=false;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		slidebutton_clicked=false;

	}

	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		//setContentView(R.layout.songlistfragment_xml);


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.songlistfragment_xml,container,false);
		context = getActivity();
		activity = getActivity();
		alphaDown = new AlphaAnimation(1.0f, 0.3f);
		alphaUp = new AlphaAnimation(0.3f, 1.0f);
		alphaDown.setDuration(1000);
		alphaUp.setDuration(1000);
		alphaDown.setFillAfter(true);
		alphaUp.setFillAfter(true);
		slidebutton_clicked=false;
		lv = (ListView) v.findViewById(R.id.listView);
		//slide_songlist_button = (Button) v.findViewById(R.id.slide_songlist_button);
		//songlist_header_textview = (TextView) v.findViewById(R.id.songslist_header_textview);
		//pb = (ProgressBar) v.findViewById(R.id.songload_progressBar);
		//pb = (ProgressBar) v.findViewById(R.id.songload_progressBar);
		database = new SongInfoDatabase(context);
		util = new Util();
		songList = new ArrayList<SongInfo>();
		//song_search_button = (ImageButton) v.findViewById(R.id.song_search_button);
		/*song_search_button.setOnClickListener(this);*/
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

	public class DatabaseThread extends Thread {

		String search;
		ViewHolder holder;
		SongInfo songInfo;

		public String getSearch() {
			return search;
		}

		public void setHolder(ViewHolder holder) {
			// TODO Auto-generated method stub
			this.holder = holder;
		}

		public void setSearch(String search) {
			this.search = search;
		}

		public DatabaseThread() {
			songInfo = new SongInfo();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//Log.i("DATABASE THREAD", search);

			switch (SWITCH_VIEW) {
			case SONG_VIEW:
				database.open();
				songList = database.searchSong_byName(search);
				// database.close();
				//Log.i("DATABASE THREAD", songList.size() + "");
				// TODO Auto-generated method stub
				if (songList.size() == 0) {
					songInfo.setTitle("No Songs Found");
					songList.add(songInfo);
					removeGestureListener();

				} else {
					addGestureListener();
				}
				adapter = new SongsListAdapter(activity, songList, lv);
				lv.setAdapter(adapter);
				break;
			case ALBUMS_VIEW:
				database.open();
				all_playlists = new ArrayList<ArrayList<SongInfo>>();
				all_playlists = database.searchSongs_albums(search);
				// database.close();
				if (all_playlists.size() == 0) {
					songInfo.setTitle("No Songs Found");
					songList.clear();
					songList.add(songInfo);
					all_playlists.add(songList);
					removeGestureListener();
				} else {
					addGestureListener();
				}
				common_list_adapter = new CommonListAdapter(activity,
						all_playlists, lv, SWITCH_VIEW, playlistChangedListener);
				lv.setAdapter(common_list_adapter);
				common_list_adapter.notifyDataSetChanged();

				break;
			case ARTIST_VIEW:
				database.open();
				all_playlists = new ArrayList<ArrayList<SongInfo>>();
				all_playlists = database.searchSongs_artists(search);
				// database.close();
				if (all_playlists.size() == 0) {
					songInfo.setTitle("No Songs Found");
					songList.clear();
					songList.add(songInfo);
					all_playlists.add(songList);
					removeGestureListener();
				} else {
					addGestureListener();
				}
				common_list_adapter = new CommonListAdapter(activity,
						all_playlists, lv, SWITCH_VIEW, playlistChangedListener);
				lv.setAdapter(common_list_adapter);
				common_list_adapter.notifyDataSetChanged();

				break;
			case PLAYLIST_VIEW:
				database.open();
				all_playlists = new ArrayList<ArrayList<SongInfo>>();
				all_playlists = database.searchSongs_playlists(search);
				// database.close();
				if (all_playlists.size() == 0) {
					songInfo.setTitle("No Songs Found");
					songList.clear();
					songList.add(songInfo);
					all_playlists.add(songList);
					removeGestureListener();
				} else {
					addGestureListener();
				}
				common_list_adapter = new CommonListAdapter(activity,
						all_playlists, lv, SWITCH_VIEW, playlistChangedListener);
				lv.setAdapter(common_list_adapter);
				common_list_adapter.notifyDataSetChanged();
				break;
			}

		}

		private void addGestureListener() {
			// TODO Auto-generated method stub
			lv.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View view, MotionEvent e) {
					detector.onTouchEvent(e);
					return false;
				}
			});

		}

	}

}
