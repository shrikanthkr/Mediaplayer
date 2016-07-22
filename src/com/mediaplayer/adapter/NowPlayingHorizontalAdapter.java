package com.mediaplayer.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.com.SongsManager;
import com.mediaplayer.manager.BroadcastManager;


public class NowPlayingHorizontalAdapter extends  RecyclerView.Adapter<NowPlayingHorizontalAdapter.ViewHolder>  {

	ArrayList<SongInfo> songArray;
	Context context;
	RecyclerView rv;
	public NowPlayingHorizontalAdapter(ArrayList<SongInfo> songArray, RecyclerView rv, Activity activity) {
		this.songArray = songArray;
		context = activity;
		this.rv = rv;
		rv.setOnClickListener(clickListener);
	}


	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = rv.getChildPosition(v);
			SongInfo songInfo = songArray.get(position);
			Intent playSong = new Intent(BroadcastManager.PLAYSONG);
			Bundle b= new Bundle();
			b.putSerializable(BroadcastManager.SONG_KEY, songInfo);
			playSong.putExtras(b);
			LocalBroadcastManager.getInstance(context).sendBroadcast(playSong);
		}
	};


	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.nowplaying_horizonal_songitem, parent, false);
		ViewHolder vh = new ViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		if(SongsManager.getInstance().getCurrentSongInfo()!=null
				&& songArray.get(position).getId()  == SongsManager.getInstance().getCurrentSongInfo().getId()){
			holder.songName.setTextColor(context.getResources().getColor(R.color.base_dark));
		}else{
			holder.songName.setTextColor(context.getResources().getColor(R.color.light_text_color));

		}
	}

	@Override
	public int getItemCount() {
		return songArray.size();
	}

	public void addAll(ArrayList<SongInfo> horizontal_songInfo_array) {
		if(songArray == null){
			songArray = new ArrayList<>();
		}
		songArray.clear();
		songArray.addAll(horizontal_songInfo_array);
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		TextView songName;
		public ViewHolder(View v) {
			super(v);
			songName = (TextView)v.findViewById(R.id.song_name_textView);
		}
	}
}
