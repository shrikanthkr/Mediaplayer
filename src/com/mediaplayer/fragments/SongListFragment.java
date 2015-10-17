package com.mediaplayer.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.mediaplayer.adapter.CommonListAdapter;
import com.mediaplayer.adapter.SongsListAdapter;
import com.mediaplayer.adapter.SongsListAdapter.ViewHolder;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.com.SongsManager;
import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.listener.PlaylistChangedListener;
import com.mediaplayer.manager.BroadcastManager;
import com.mediaplayer.utility.SongsHolder;
import com.mediaplayer.utility.Util;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirections;

public class SongListFragment extends MediaFragment implements SearchView.OnQueryTextListener {
	ListView lv;
	SearchView searchView;
	Context context;
	SongsListAdapter adapter;
	ArrayList<SongInfo> songList;
	GestureDetector detector;
	SongInfoDatabase database;
	Activity activity;
	Util util;
	SwipeActionAdapter swipeActionAdapter;

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
		songList = new ArrayList<>();
		context = getActivity();
		activity = getActivity();
		database =  SongInfoDatabase.getInstance();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.songlistfragment_xml,container,false);
		lv = (ListView) v.findViewById(R.id.listView);
		populateSonglist();
		return v;
	}

	@Override
	public void setTitle() {
		getActivity().setTitle(getString(R.string.songs));
	}

	public void populateSonglist() {
		songList = database.getSongs(null);
		adapter = new SongsListAdapter(getActivity(), songList, lv);
		lv.setTextFilterEnabled(true);
		lv.setSelection(SongsHolder.smoothScrollTo);
		lv.setFastScrollEnabled(true);
		swipeActionAdapter = new SwipeActionAdapter(adapter);
		swipeActionAdapter.setListView(lv);
		swipeActionAdapter.addBackground(SwipeDirections.DIRECTION_FAR_LEFT,R.layout.row_bg_left_far)
				.addBackground(SwipeDirections.DIRECTION_NORMAL_LEFT,R.layout.row_bg_left)
				.addBackground(SwipeDirections.DIRECTION_FAR_RIGHT,R.layout.row_bg_right_far)
				.addBackground(SwipeDirections.DIRECTION_NORMAL_RIGHT, R.layout.row_bg_right);

		swipeActionAdapter.setSwipeActionListener(swipeListener);
		lv.setAdapter(swipeActionAdapter);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int id, long l) {
				SongInfo  info  = getSelectedSong(id);
				Intent playSong = new Intent(BroadcastManager.PLAYSONG);
				Bundle b= new Bundle();
				b.putSerializable(BroadcastManager.SONG_KEY, info);
				playSong.putExtras(b);

				LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(playSong);
			}
		});
	}

	private SongInfo getSelectedSong(int id){
		SongInfo songInfo = new SongInfo();
		songInfo.setAlbum(songList.get(id).getAlbum());
		songInfo.setAlbum_art(songList.get(id).getAlbum_art());
		songInfo.setAlbum_id(songList.get(id).getAlbum_id());
		songInfo.setArtist(songList.get(id).getArtist());
		songInfo.setData(songList.get(id).getData());
		songInfo.setDisplayName(songList.get(id).getDisplayName());
		songInfo.setDuration(songList.get(id).getDuration());
		songInfo.setId(songList.get(id).getId());
		songInfo.setTitle(songList.get(id).getTitle());
		return songInfo;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SongsHolder.smoothScrollTo = lv.getFirstVisiblePosition();
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
		songList = database.getSongs(search);
		adapter.addAll(songList);
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		searchView = (SearchView)menu.findItem(R.id.search).getActionView();
		searchView.setOnQueryTextListener(this);
	}
	SwipeActionAdapter.SwipeActionListener swipeListener = new SwipeActionAdapter.SwipeActionListener(){
		@Override
		public boolean hasActions(int position){
			// All items can be swiped
			return true;
		}

		@Override
		public boolean shouldDismiss(int position, int direction){
			// Only dismiss an item when swiping normal left
			return false;
		}

		@Override
		public void onSwipe(int[] positionList, int[] directionList){
			for(int i=0;i<positionList.length;i++) {
				int direction = directionList[i];
				int position = positionList[i];
				String dir = "";

				switch (direction) {
					case SwipeDirections.DIRECTION_FAR_LEFT:
					case SwipeDirections.DIRECTION_NORMAL_LEFT:
						break;
					case SwipeDirections.DIRECTION_FAR_RIGHT:
					case SwipeDirections.DIRECTION_NORMAL_RIGHT:
						SongInfo info = getSelectedSong(position);
						SongsManager.getInstance().addSong(info);
						break;
				}
				//swipeActionAdapter.notifyDataSetChanged();
			}
		}
	};

}
