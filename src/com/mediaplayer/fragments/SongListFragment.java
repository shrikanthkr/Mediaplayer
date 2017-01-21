package com.mediaplayer.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.mediaplayer.adapter.SongsListAdapter;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.interfaces.RecyclerClickHelper;
import com.mediaplayer.manager.BroadcastManager;
import com.mediaplayer.utility.Util;

import java.util.ArrayList;

public class SongListFragment extends BaseFragment implements SearchView.OnQueryTextListener {
	RecyclerView rv;
	SearchView searchView;
	Context context;
	SongsListAdapter adapter;
	ArrayList<SongInfo> songList;
	SongInfoDatabase database;
	Activity activity;
	Util util;
	private RecyclerView.LayoutManager mLayoutManager;

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
		rv = (RecyclerView) v.findViewById(R.id.recycler_view);
		// use a linear layout manager
		mLayoutManager = new LinearLayoutManager(getActivity());
		rv.setLayoutManager(mLayoutManager);
		populateSonglist();
		return v;
	}

	@Override
	public void setTitle() {
		getActivity().setTitle(getString(R.string.songs));
	}

	public void populateSonglist() {
		songList = database.getSongs(null);
		adapter = new SongsListAdapter(getActivity(), songList, rv, clickHelper);
		/*rv.setTextFilterEnabled(true);
		rv.setFastScrollEnabled(true);*/

		rv.setAdapter(adapter);
	}

	RecyclerClickHelper clickHelper = new RecyclerClickHelper() {
		@Override
		public void onItemClickListener(View view, int position) {
			SongInfo  info  = getSelectedSong(position);
			Intent playSong = new Intent(BroadcastManager.PLAYSONG);
			Bundle b= new Bundle();
			b.putSerializable(BroadcastManager.SONG_KEY, info);
			playSong.putExtras(b);

			LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(playSong);
		}
	};

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
//		searchView = (SearchView)menu.findItem(R.id.search).getActionView();
//		searchView.setOnQueryTextListener(this);
	}
	/*SwipeActionAdapter.SwipeActionListener swipeListener = new SwipeActionAdapter.SwipeActionListener(){
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
	};*/

}
